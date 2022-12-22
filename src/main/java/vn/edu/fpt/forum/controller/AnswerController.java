package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.request.answer.VoteAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToAnswerRequest;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToAnswerResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 08:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/answers")
public interface AnswerController {

    @PostMapping("/{answer-id}")
    ResponseEntity<GeneralResponse<AddCommentToAnswerResponse>> addCommentToAnswer(@PathVariable(name = "answer-id") String answerId, @RequestBody AddCommentToAnswerRequest request);

    @PutMapping("/{answer-id}")
    ResponseEntity<GeneralResponse<Object>> updateAnswer(@PathVariable(name = "answer-id") String answerId, @RequestBody UpdateAnswerRequest request);

    @DeleteMapping("/{answer-id}/{comment-id}")
    ResponseEntity<GeneralResponse<Object>> deleteCommentFromAnswer(@PathVariable(name = "answer-id") String answerId, @PathVariable(name = "comment-id") String commentId);

    @PutMapping("/{answer-id}/vote")
    ResponseEntity<GeneralResponse<Object>> voteAnswer(@PathVariable(name = "answer-id") String answerId, @RequestBody VoteAnswerRequest request);

}
