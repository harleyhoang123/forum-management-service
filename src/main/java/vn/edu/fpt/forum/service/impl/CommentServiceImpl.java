package vn.edu.fpt.forum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.config.kafka.producer.HandleNotifyProducer;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.dto.event.HandleNotifyEvent;
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
import vn.edu.fpt.forum.service.UserInfoService;

import java.time.LocalDateTime;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final HandleNotifyProducer handleNotifyProducer;
    private final UserInfoService userInfoService;

    @Override
    public AddCommentToAnswerResponse addCommentToAnswer(String answerId, AddCommentToAnswerRequest request) {
        Answer answer = answerRepository.findById(answerId)
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
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow(() -> new BusinessException("Can't account id from token"));
        handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                .accountId(answer.getCreatedBy())
                .content(userInfoService.getUserInfo(accountId).getFullName() + " commented your answer")
                .createdDate(LocalDateTime.now())
                .build());
        return AddCommentToAnswerResponse.builder()
                .commentId(comment.getCommentId())
                .build();
    }

    @Override
    public AddCommentToQuestionResponse addCommentToQuestion(String commentId, AddCommentToQuestionRequest request) {
        Question question = questionRepository.findById(commentId)
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
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow(() -> new BusinessException("Can't account id from token"));
        handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                .accountId(question.getCreatedBy())
                .content(userInfoService.getUserInfo(accountId).getFullName() + " commented your question")
                .createdDate(LocalDateTime.now())
                .build());
        return AddCommentToQuestionResponse.builder()
                .commentId(comment.getCommentId())
                .build();
    }

    @Override
    public void deleteCommentFromQuestion(String questionId, String commentId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Question ID not exist"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment ID not exist"));
        if(comment.getScore() > 0){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment already has score");
        }
        List<Comment> comments = question.getComments();
        if (comments.stream().noneMatch(m->m.getCommentId().equals(commentId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment not exist in Question");
        }
        comments.removeIf(m->m.getCommentId().equals(commentId));
        question.setComments(comments);
        try{
            commentRepository.deleteById(commentId);
            log.info("Delete comment success");
        }catch (Exception ex){
            throw new BusinessException("Can't delete comment by id: "+ ex.getMessage());
        }
        try{
            questionRepository.save(question);
            log.info("Save question success");
        }catch (Exception ex){
            throw new BusinessException("Can't save question: "+ ex.getMessage());
        }
    }

    @Override
    public void deleteCommentFromAnswer(String answerId, String commentId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Answer ID not exist"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment ID not exist"));
        if(comment.getScore() > 0){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment already has score");
        }
        List<Comment> comments = answer.getComments();
        if (comments.stream().noneMatch(m->m.getCommentId().equals(commentId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Comment not exist in Answer");
        }
        comments.removeIf(m->m.getCommentId().equals(commentId));
        answer.setComments(comments);
        try{
            commentRepository.deleteById(commentId);
            log.info("Delete comment success");
        }catch (Exception ex){
            throw new BusinessException("Can't delete comment by id: "+ ex.getMessage());
        }
        try{
            answerRepository.save(answer);
            log.info("Save answer success");
        }catch (Exception ex){
            throw new BusinessException("Can't save answer: "+ ex.getMessage());
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
