package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.request.comment.AddCommentToAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToQuestionRequest;
import vn.edu.fpt.forum.dto.request.comment.UpdateCommentRequest;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToQuestionResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 11:13
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface CommentService {

    AddCommentToAnswerResponse addCommentToAnswer(String answerId, AddCommentToAnswerRequest request);

    AddCommentToQuestionResponse addCommentToQuestion(String questionId, AddCommentToQuestionRequest request);

    void deleteComment(String commentId);

    void updateComment(String commentId, UpdateCommentRequest request);

    void voteComment(String commentId, Boolean isIncrease);
}
