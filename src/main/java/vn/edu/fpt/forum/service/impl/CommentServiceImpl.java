package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToQuestionRequest;
import vn.edu.fpt.forum.dto.request.comment.UpdateCommentRequest;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToQuestionResponse;
import vn.edu.fpt.forum.entity.Answer;
import vn.edu.fpt.forum.entity.Comment;
import vn.edu.fpt.forum.entity.Question;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.AnswerRepository;
import vn.edu.fpt.forum.repository.CommentRepository;
import vn.edu.fpt.forum.repository.QuestionRepository;
import vn.edu.fpt.forum.service.CommentService;

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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public AddCommentToAnswerResponse addCommentToAnswer(AddCommentToAnswerRequest request) {
        Answer answer = answerRepository.findById(request.getAnswerId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .build();
        try {
            comment = commentRepository.save(comment);
        }catch (Exception ex){
            throw new BusinessException("Can't create comment: "+ ex.getMessage());
        }

        List<Comment> currentComment = answer.getComments();
        currentComment.add(comment);
        answer.setComments(currentComment);
        try {
            answerRepository.save(answer);
        }catch (Exception ex){
            throw new BusinessException("Can't add comment to answer: "+ ex.getMessage());
        }

        return AddCommentToAnswerResponse.builder()
                .commentId(comment.getCommentId())
                .build();
    }

    @Override
    public AddCommentToQuestionResponse addCommentToQuestion(AddCommentToQuestionRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .build();
        try {
            comment = commentRepository.save(comment);
        }catch (Exception ex){
            throw new BusinessException("Can't create comment: "+ ex.getMessage());
        }

        List<Comment> currentComments = question.getComments();
        currentComments.add(comment);
        question.setComments(currentComments);

        try {
            questionRepository.save(question);
        }catch (Exception ex){
            throw new BusinessException("Can't add comment to question: "+ ex.getMessage());
        }
        return AddCommentToQuestionResponse.builder()
                .commentId(comment.getCommentId())
                .build();
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment ID not exist"));
        if(comment.getScore() > 0){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment already has score");
        }
        try{
            commentRepository.deleteById(commentId);
            log.info("Delete comment success");
        }catch (Exception ex){
            throw new BusinessException("Can't delete comment by id: "+ ex.getMessage());
        }
    }

    @Override
    public void updateComment(String commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment ID not exist"));
        if(Objects.nonNull(request.getContent())) {
            comment.setContent(request.getContent());
            try {
                commentRepository.save(comment);
                log.info("Update comment success");
            }catch (Exception ex){
                throw new BusinessException("Can't update comment: "+ ex.getMessage());
            }
        }else{
            log.info("No data must update");
        }
    }

    @Override
    public void voteComment(String commentId, Boolean isIncrease) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment ID not exist"));
        if(isIncrease){
            comment.setScore(comment.getScore()+1);
        }else{
            comment.setScore(comment.getScore()-1);
        }
        try {
            commentRepository.save(comment);
            log.info("Change comment score success");
        }catch (Exception ex){
            throw new BusinessException("Can't change comment score: "+ ex.getMessage());
        }
    }
}
