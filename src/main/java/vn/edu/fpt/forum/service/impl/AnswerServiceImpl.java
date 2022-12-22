package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.constant.AnswerStatusEnum;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.VoteAnswerRequest;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;
import vn.edu.fpt.forum.entity.Answer;
import vn.edu.fpt.forum.entity.Question;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.AnswerRepository;
import vn.edu.fpt.forum.repository.QuestionRepository;
import vn.edu.fpt.forum.service.AnswerService;

import java.util.List;
import java.util.Objects;

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
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        if(answer.getScore() > 0){
            throw new BusinessException("Can't delete answer already has score");
        }
        List<Answer> answers = question.getAnswers();
        if (answers.stream().noneMatch(m->m.getAnswerId().equals(answerId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer not exist in Question");
        }
        answers.removeIf(m->m.getAnswerId().equals(answerId));
        question.setAnswers(answers);
        try{
            answerRepository.deleteById(answerId);
            log.info("Delete answer success");
        }catch (Exception ex){
            throw new BusinessException("Can't delete answer: "+ ex.getMessage());
        }
        try{
            questionRepository.save(question);
            log.info("Save question success");
        }catch (Exception ex){
            throw new BusinessException("Can't save question: "+ ex.getMessage());
        }
    }

    @Override
    public void voteAnswer(String answerId, VoteAnswerRequest request) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));

        if(request.getStatus().equals(AnswerStatusEnum.APPROVED.getStatusName())){
            answer.setStatus(AnswerStatusEnum.APPROVED);
        }else{
            answer.setStatus(AnswerStatusEnum.REJECTED);
        }
        try{
            answerRepository.save(answer);
            log.info("Vote answer success");
        }catch (Exception ex){
            throw new BusinessException("Can't vote answer: "+ ex.getMessage());
        }
    }
}
