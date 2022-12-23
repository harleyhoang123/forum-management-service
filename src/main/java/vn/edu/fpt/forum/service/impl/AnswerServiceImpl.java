package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.constant.AnswerStatusEnum;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.constant.VoteActionEnum;
import vn.edu.fpt.forum.constant.VoteStatusEnum;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.VoteAnswerRequest;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;
import vn.edu.fpt.forum.dto.response.answer.GetAnswerResponse;
import vn.edu.fpt.forum.dto.response.answer.VoteAnswerResponse;
import vn.edu.fpt.forum.entity.Answer;
import vn.edu.fpt.forum.entity.Question;
import vn.edu.fpt.forum.entity.VotedUser;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.AnswerRepository;
import vn.edu.fpt.forum.repository.QuestionRepository;
import vn.edu.fpt.forum.service.AnswerService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 11:13
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public CreateAnswerResponse createAnswer(String questionId, CreateAnswerRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        List<Answer> currentAnswer = question.getAnswers();

        Answer answer = Answer.builder()
                .content(request.getContent())
                .build();
        try {
            answer = answerRepository.save(answer);
            log.info("Update answer success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save answer to database: " + ex.getMessage());
        }

        currentAnswer.add(answer);

        question.setAnswers(currentAnswer);

        try {
            questionRepository.save(question);
            log.info("Update question success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save question to database: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void updateAnswer(String answerId, UpdateAnswerRequest request) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));

        if (Objects.nonNull(request.getContent())) {
            answer.setContent(request.getContent());

            try {
                answerRepository.save(answer);
                log.info("Update answer success");
            } catch (Exception ex) {
                throw new BusinessException("Can't update answer: " + ex.getMessage());
            }
        }
    }

    @Override
    public void deleteAnswer(String questionId, String answerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        if (answer.getScore() > 0) {
            throw new BusinessException("Can't delete answer already has score");
        }
        List<Answer> answers = question.getAnswers();
        if (answers.stream().noneMatch(m -> m.getAnswerId().equals(answerId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer not exist in Question");
        }
        answers.removeIf(m -> m.getAnswerId().equals(answerId));
        question.setAnswers(answers);
        try {
            answerRepository.deleteById(answerId);
            log.info("Delete answer success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete answer: " + ex.getMessage());
        }
        try {
            questionRepository.save(question);
            log.info("Save question success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save question: " + ex.getMessage());
        }
    }

    @Override
    public void acceptAnswer(String questionId, String answerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        List<Answer> answers = question.getAnswers();
        Optional<Answer> acceptedAnswer = answers.stream().filter(m -> m.getStatus().equals(AnswerStatusEnum.ACCEPTED)).findFirst();
        if (acceptedAnswer.isPresent()) {
            if (answerId.equals(acceptedAnswer.get().getAnswerId())) {
                //acceptedAnswer.get().setStatus(AnswerStatusEnum.NOT_ACCEPTED);
                answer.setStatus(AnswerStatusEnum.NOT_ACCEPTED);
            } else {
                acceptedAnswer.get().setStatus(AnswerStatusEnum.NOT_ACCEPTED);
                answer.setStatus(AnswerStatusEnum.ACCEPTED);
            }
            try {
                answerRepository.save(acceptedAnswer.get());
                log.info("Vote answer success");
            } catch (Exception ex) {
                throw new BusinessException("Can't vote answer: " + ex.getMessage());
            }
        } else {
            answer.setStatus(AnswerStatusEnum.ACCEPTED);
        }

        try {
            answerRepository.save(answer);
            log.info("Vote answer success");
        } catch (Exception ex) {
            throw new BusinessException("Can't vote answer: " + ex.getMessage());
        }

    }

    @Override
    public VoteAnswerResponse voteAnswer(String answerId, VoteAnswerRequest request) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        List<VotedUser> votedUsers = answer.getVotedUsers();
        VoteAnswerResponse response = VoteAnswerResponse.builder()
                .action(VoteActionEnum.NO_ACTION)
                .build();
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow(() -> new BusinessException("Can't account id from token"));
        Integer currentScore = answer.getScore();
        Optional<VotedUser> votedUser = votedUsers.stream().filter(m -> m.getAccountId().equals(accountId)).findFirst();
        if (!votedUser.isPresent()) {
            if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                answer.setScore(currentScore + 1);
                votedUsers.add(VotedUser.builder()
                        .accountId(accountId)
                        .status(VoteStatusEnum.LIKED)
                        .build());
                answer.setVotedUsers(votedUsers);
                response.setAction(VoteActionEnum.LIKE);
            } else {
                answer.setScore(currentScore - 1);
                votedUsers.add(VotedUser.builder()
                        .accountId(accountId)
                        .status(VoteStatusEnum.DISLIKED)
                        .build());
                answer.setVotedUsers(votedUsers);
                response.setAction(VoteActionEnum.DISLIKE);
            }
            answer.setVotedUsers(votedUsers);
            try {
                answerRepository.save(answer);
                log.info("Vote success");
            } catch (Exception ex) {
                throw new BusinessException("Vote fail: " + ex.getMessage());
            }

        } else {
            votedUsers.remove(votedUser.get());
            if (votedUser.get().getStatus().getStatus().equals(request.getStatus())) {
                if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                    answer.setScore(currentScore - 1);
                    answer.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.UN_LIKE);
                } else {
                    answer.setScore(currentScore + 1);
                    answer.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.UN_DISLIKE);
                }
            } else {
                if (request.getStatus().equals(VoteStatusEnum.LIKED.getStatus())) {
                    answer.setScore(currentScore + 2);
                    votedUsers.add(VotedUser.builder()
                            .accountId(accountId)
                            .status(VoteStatusEnum.LIKED)
                            .build());
                    answer.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.LIKE);
                } else {
                    answer.setScore(currentScore - 2);
                    votedUsers.add(VotedUser.builder()
                            .accountId(accountId)
                            .status(VoteStatusEnum.DISLIKED)
                            .build());
                    answer.setVotedUsers(votedUsers);
                    response.setAction(VoteActionEnum.DISLIKE);
                }
            }
            answer.setVotedUsers(votedUsers);
            try {
                answerRepository.save(answer);
                log.info("Vote success");
            } catch (Exception ex) {
                throw new BusinessException("Vote fail: " + ex.getMessage());
            }
        }
        return response;
    }

    @Override
    public GetAnswerResponse getAnswerDetail(String answerId) {
        if (!ObjectId.isValid(answerId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist");
        }
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow(() -> new BusinessException("Can't account id from token"));
        Optional<VotedUser> votedUser = answer.getVotedUsers().stream().filter(m->m.getAccountId().equals(accountId)).findFirst();
        GetAnswerResponse getAnswerResponse = GetAnswerResponse.builder()
                .answerId(answerId)
                .content(answer.getContent())
                .score(answer.getScore())
                .status(answer.getStatus())
                .build();
        if (!votedUser.isPresent()) {
            getAnswerResponse.setVotedStatus(VoteStatusEnum.UN_VOTE);
        } else {
            getAnswerResponse.setVotedStatus(votedUser.get().getStatus());
        }
        return getAnswerResponse;
    }
}
