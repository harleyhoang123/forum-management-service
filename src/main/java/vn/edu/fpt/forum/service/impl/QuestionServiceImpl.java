package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.regex.qual.Regex;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.config.kafka.producer.SendEmailProducer;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.dto.cache.UserInfo;
import vn.edu.fpt.forum.dto.common.AskedInfo;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.event.SendEmailEvent;
import vn.edu.fpt.forum.dto.feign.response.GetAccountIdByUsernameResponse;
import vn.edu.fpt.forum.dto.request.question.CreateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.GetQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.UpdateQuestionRequest;
import vn.edu.fpt.forum.dto.response.answer.GetAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.GetCommentResponse;
import vn.edu.fpt.forum.dto.response.question.CreateQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionDetailResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionResponse;
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
                .content(request.getContent())
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

        AccountActivity accountActivity = AccountActivity.builder()
                .accountId(question.getCreatedBy())
                .build();
        try {
            accountActivityRepository.save(accountActivity);
            log.info("Create account activity success");
        }catch (Exception ex){
            throw new BusinessException("Can't create account activity in database: "+ ex.getMessage());
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
        if(Objects.nonNull(request.getContent())){
            question.setContent(request.getContent());
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
    public void voteQuestion(String questionId, Boolean isIncrease) {

    }

    @Override
    public void closeQuestion(String questionId) {

    }

    @Override
    public PageableResponse<GetQuestionResponse> getQuestionByCondition(GetQuestionRequest request) {
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
                .content(question.getContent())
                .views(question.getViews())
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
                .content(question.getContent())
                .tags(question.getTags())
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
