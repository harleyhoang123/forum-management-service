package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.checkerframework.checker.regex.qual.Regex;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.config.kafka.producer.SendEmailProducer;
import vn.edu.fpt.forum.constant.QuestionStatusEnum;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.constant.VoteActionEnum;
import vn.edu.fpt.forum.constant.VoteStatusEnum;
import vn.edu.fpt.forum.dto.cache.UserInfo;
import vn.edu.fpt.forum.dto.common.AskedInfo;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.event.SendEmailEvent;
import vn.edu.fpt.forum.dto.feign.response.GetAccountIdByUsernameResponse;
import vn.edu.fpt.forum.dto.request.question.*;
import vn.edu.fpt.forum.dto.response.answer.GetAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.GetCommentResponse;
import vn.edu.fpt.forum.dto.response.question.CreateQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionDetailResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.VoteQuestionResponse;
import vn.edu.fpt.forum.entity.*;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.*;
import vn.edu.fpt.forum.service.QuestionService;
import vn.edu.fpt.forum.service.UserInfoService;
import vn.edu.fpt.forum.service.feign.AuthenticationFeignClient;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 22/11/2022 - 19:45
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AppConfigRepository appConfigRepository;
    private final SendEmailProducer sendEmailProducer;
    private final UserInfoService userInfoService;
    private final AuthenticationFeignClient authenticationFeignClient;
    private final MongoTemplate mongoTemplate;
    private final AccountActivityRepository accountActivityRepository;

    @Override
    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question = Question.builder()
                .title(request.getTitle())
                .problem(request.getProblem())
                .triedCase(request.getTriedCase())
                .tags(request.getTag())
                .build();

        try {
            question = questionRepository.save(question);
            log.info("Save question success");
        }catch (Exception ex){
            log.error("Error when save question to database: {}", ex.getMessage());
            throw new BusinessException("Can't save question to database: "+ ex.getMessage());
        }

        if(Boolean.FALSE.equals(question.getTags().isEmpty())){
            notifyTagOwner(question);
        }

        return CreateQuestionResponse.builder()
                .questionId(question.getQuestionId())
                .build();
    }

    private void notifyTagOwner(Question question) {
        log.info("Notify to tag owner: {}", question);
        List<Tag> tags = tagRepository.findAllByTagNameIn(question.getTags());
        if(tags.isEmpty()){
            log.info("Tag not register: {}", question.getTags());
        }else{
            Optional<AppConfig> noticeQuestionConfigOptional = appConfigRepository.findByConfigKey("NOTICE_QUESTION_OWNER_TEMP_ID");
            String questionId = question.getQuestionId();
            if(noticeQuestionConfigOptional.isPresent()){
                AppConfig noticeQuestionConfig = noticeQuestionConfigOptional.get();
                tags.forEach(tag -> {
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .templateId(noticeQuestionConfig.getConfigValue())
                            .sendTo(tag.getOwnerBy())
                            .bcc(null)
                            .cc(null)
                            .params(Map.of("QUESTION_ID", questionId))
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                });
            }else{
                log.info("App config missing key: NOTICE_QUESTION_OWNER_TEMP_ID, can't send email to tag owner");
            }
        }
    }

    @Override
    public void updateQuestion(String questionId, UpdateQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        boolean isUpdated = false;
        boolean isChangeTagOwner = false;
        if(Objects.nonNull(request.getTitle())){
            question.setTitle(request.getTitle());
            isUpdated = true;
        }
        if(Objects.nonNull(request.getProblem())){
            question.setProblem(request.getProblem());
            isUpdated = true;
        }
        if(Objects.nonNull(request.getTriedCase())){
            question.setTriedCase(request.getTriedCase());
            isUpdated = true;
        }
        if(Objects.nonNull(request.getTags())){
            question.setTags(request.getTags());
            isUpdated = true;
            isChangeTagOwner = true;
        }

        if(isUpdated){
            log.info("Question is updated");
            try {
                question = questionRepository.save(question);
                if(isChangeTagOwner){
                    log.info("Notify question to tag owner: {}", question);
                    notifyTagOwner(question);
                }
            }catch (Exception ex){
                throw new BusinessException("Can't update question in database: "+ ex.getMessage());
            }
        }
    }

    @Override
    public void deleteQuestion(String questionId) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        try {
            questionRepository.deleteById(questionId);
            log.info("Delete question success: {}", questionId);
        }catch (Exception ex){
            throw new BusinessException("Can't delete question by ID: "+ ex.getMessage());
        }
    }

    @Override
    public VoteQuestionResponse voteQuestion(String questionId, VoteQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        List<VotedUser> votedUsers = question.getVotedUsers();
        VoteQuestionResponse response = VoteQuestionResponse.builder()
                .action(VoteActionEnum.NO_ACTION)
                .build();
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow(() -> new BusinessException("Can't account id from token"));
        Integer currentScore = question.getScore();
        Optional<VotedUser> votedUser = votedUsers.stream().filter(m->m.getAccountId().equals(accountId)).findFirst();
        if (!votedUser.isPresent()) {
            if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                question.setScore(currentScore+1);
                votedUsers.add(VotedUser.builder()
                                .accountId(accountId)
                                .status(VoteStatusEnum.LIKED)
                        .build());
                question.setVotedUsers(votedUsers);
                response.setAction(VoteActionEnum.LIKE);
            } else {
                question.setScore(currentScore-1);
                votedUsers.add(VotedUser.builder()
                        .accountId(accountId)
                        .status(VoteStatusEnum.DISLIKED)
                        .build());
                question.setVotedUsers(votedUsers);
                response.setAction(VoteActionEnum.DISLIKE);
            }
            question.setVotedUsers(votedUsers);
            try {
                questionRepository.save(question);
                log.info("Vote success");
            }catch (Exception ex){
                throw new BusinessException("Vote fail: "+ ex.getMessage());
            }

        } else {
            votedUsers.remove(votedUser.get());
            if (votedUser.get().getStatus().getStatus().equals(request.getStatus())) {
                if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                    question.setScore(currentScore-1);
                    question.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.UN_LIKE);
                } else {
                    question.setScore(currentScore+1);
                    question.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.UN_DISLIKE);
                }
            } else {
                if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                    question.setScore(currentScore+2);
                    votedUsers.add(VotedUser.builder()
                            .accountId(accountId)
                            .status(VoteStatusEnum.LIKED)
                            .build());
                    question.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.LIKE);
                } else {
                    question.setScore(currentScore-2);
                    votedUsers.add(VotedUser.builder()
                            .accountId(accountId)
                            .status(VoteStatusEnum.DISLIKED)
                            .build());
                    question.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.DISLIKE);
                }
            }
            question.setVotedUsers(votedUsers);
            try {
                questionRepository.save(question);
                log.info("Vote success");
            }catch (Exception ex){
                throw new BusinessException("Vote fail: "+ ex.getMessage());
            }
        }
        return response;
    }

    @Override
    public void closeQuestion(String questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        question.setStatus(QuestionStatusEnum.CLOSE.getStatusName());
        try {
            questionRepository.save(question);
            log.info("Close question success: {}", questionId);
        }catch (Exception ex){
            throw new BusinessException("Can't close question by ID: "+ ex.getMessage());
        }
    }

    @Override
    public PageableResponse<GetQuestionResponse> getQuestionByCondition(GetQuestionRequest request) {
        Query query = new Query();
        if (Objects.nonNull(request.getQuestionId())) {
            query.addCriteria(Criteria.where("_id").is(request.getQuestionId()));
        }
        if (Objects.nonNull(request.getTitle())) {
            query.addCriteria(Criteria.where("title").regex(request.getTitle()));
        }
        if (Objects.nonNull(request.getTag())) {
            query.addCriteria(Criteria.where("tags").regex(request.getTag()));
        }
        if (Objects.nonNull(request.getProblem())) {
            query.addCriteria(Criteria.where("problem").is(request.getProblem()));
        }
        if (Objects.nonNull(request.getTriedCase())) {
            query.addCriteria(Criteria.where("tried_case").is(request.getTriedCase()));
        }
        if (Objects.nonNull(request.getStatus())) {
            query.addCriteria(Criteria.where("status").is(request.getStatus()));
        }
        if (Objects.nonNull(request.getCreatedBy())) {
            query.addCriteria(Criteria.where("created_by").is(request.getCreatedBy()));
        }
        BaseMongoRepository.addCriteriaWithAuditable(query, request);
        Long totalElements = mongoTemplate.count(query, Question.class);
        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);

        List<Question> questions = mongoTemplate.find(query, Question.class);
        List<GetQuestionResponse> getQuestionResponse = questions.stream().map(this::convertToGetQuestionResponse).collect(Collectors.toList());

        return new PageableResponse<>(request, totalElements, getQuestionResponse);
    }

    @Override
    public PageableResponse<GetQuestionResponse> getQuestionBySearchData(GetQuestionBySearchDataRequest request) {
        Query query = new Query();
        if(request.getSearchData().matches("^\\[[a-zA-Z0-9_]*\\]$")){
            query.addCriteria(Criteria.where("tags").in(request.getSearchData()));
        }else if(request.getSearchData().matches("^user:")){
            String username = request.getSearchData().replaceFirst("^user:", "").trim();
            try {
                ResponseEntity<GeneralResponse<GetAccountIdByUsernameResponse>> responseEntity = authenticationFeignClient.getAccountIdsByUsername(username);
                GetAccountIdByUsernameResponse response = Objects.requireNonNull(responseEntity.getBody()).getData();
                List<String> accountIds = response.getAccountIds();
                query.addCriteria(Criteria.where("created_by").in(accountIds));
            }catch (Exception ex){
                throw new BusinessException("Can't get account id from authentication service: "+ ex.getMessage());
            }

        }else {
            query.addCriteria(Criteria.where("title").regex("^(.)*("+request.getSearchData()+")(.)*$"));
        }
        Long totalElements = mongoTemplate.count(query, Question.class);

        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);
        List<Question> questions;
        try {
            questions = mongoTemplate.find(query, Question.class);
            log.info("Find question from database success");
        }catch (Exception ex){
            throw new BusinessException("Can't find question from database: "+ ex.getMessage());
        }
        List<GetQuestionResponse> getQuestionResponse = questions.stream().map(this::convertToGetQuestionResponse).collect(Collectors.toList());
        return new PageableResponse<>(request, totalElements, getQuestionResponse);
    }

    private GetQuestionResponse convertToGetQuestionResponse(Question question) {
        AccountActivity accountActivity = accountActivityRepository.findById(question.getCreatedBy())
                        .orElseThrow(() -> new BusinessException("Account activity not exist in database"));
        UserInfo userInfo = userInfoService.getUserInfo(question.getCreatedBy());
        return GetQuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .problem(question.getProblem())
                .triedCase(question.getTriedCase())
                .views(question.getViews())
                .status(question.getStatus())
                .score(question.getScore())
                .answers(question.getAnswers().size())
                .tags(question.getTags())
                .createdDate(question.getCreatedDate())
                .askedBy(AskedInfo.builder()
                        .accountId(question.getCreatedBy())
                        .username(userInfo.getUsername())
                        .score(accountActivity.getScore())
                        .build())
                .build();
    }

    @Override
    public GetQuestionDetailResponse getQuestionDetail(String questionId) {
        if(!ObjectId.isValid(questionId)){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question Id invalid");
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));

        Integer currentView = question.getViews();
        question.setViews(currentView+=1);
        try{
            questionRepository.save(question);
            log.info("Update question views success");
        }catch (Exception ex){
            throw new BusinessException("Can't update question views: "+ ex.getMessage());
        }
        return GetQuestionDetailResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .problem(question.getProblem())
                .triedCase(question.getTriedCase())
                .tags(question.getTags())
                .status(question.getStatus())
                .views(question.getViews())
                .score(question.getScore())
                .comments(question.getComments().stream().map(this::convertToGetCommentResponse).collect(Collectors.toList()))
                .answers(question.getAnswers().stream().map(this::convertToGetAnswerResponse).collect(Collectors.toList()))
                .createdBy(userInfoService.getUserInfo(question.getCreatedBy()))
                .createdDate(question.getCreatedDate())
                .lastModifiedBy(userInfoService.getUserInfo(question.getLastModifiedBy()))
                .lastModifiedDate(question.getLastModifiedDate())
                .build();
    }

    private GetAnswerResponse convertToGetAnswerResponse(Answer answer) {
        return GetAnswerResponse.builder()
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .score(answer.getScore())
                .status(answer.getStatus())
                .comments(answer.getComments().stream().map(this::convertToGetCommentResponse).collect(Collectors.toList()))
                .createdBy(userInfoService.getUserInfo(answer.getCreatedBy()))
                .createdDate(answer.getCreatedDate())
                .lastModifiedBy(userInfoService.getUserInfo(answer.getLastModifiedBy()))
                .lastModifiedDate(answer.getLastModifiedDate())
                .build();
    }

    private GetCommentResponse convertToGetCommentResponse(Comment comment){
        return GetCommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .score(comment.getScore())
                .createdBy(userInfoService.getUserInfo(comment.getCreatedBy()))
                .createdDate(comment.getCreatedDate())
                .lastModifiedBy(userInfoService.getUserInfo(comment.getLastModifiedBy()))
                .lastModifiedDate(comment.getLastModifiedDate())
                .build();
    }

}
