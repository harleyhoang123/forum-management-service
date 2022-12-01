package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.CreateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.UpdateQuestionRequest;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;
import vn.edu.fpt.forum.dto.response.comment.AddCommentToQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.CreateQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionDetailResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 08:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/questions")
public interface QuestionController {

    @PostMapping("/question")
    ResponseEntity<GeneralResponse<CreateQuestionResponse>> createQuestion(@RequestBody CreateQuestionRequest request);

    @PostMapping("/{question-id}/comment")
    ResponseEntity<GeneralResponse<AddCommentToQuestionResponse>> addCommentToQuestion(@PathVariable(name = "question-id") String questionId, @RequestBody AddCommentToQuestionRequest request);

    @PostMapping("/{question-id}/answer")
    ResponseEntity<GeneralResponse<CreateAnswerResponse>> addAnswerToQuestion(@PathVariable(name = "question-id") String questionId, @RequestBody CreateAnswerRequest request);


    @PutMapping("/{question-id}")
    ResponseEntity<GeneralResponse<Object>> updateQuestion(@PathVariable(name = "question-id") String questionId, @RequestBody UpdateQuestionRequest request);

    @PostMapping("/{question-id}")
    ResponseEntity<GeneralResponse<Object>> closeQuestion(@PathVariable(name = "question-id") String questionId);

    ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestion();

    @GetMapping("/{question-id}")
    ResponseEntity<GeneralResponse<GetQuestionDetailResponse>> getQuestionDetail(@PathVariable(name = "question-id") String questionId);

}
