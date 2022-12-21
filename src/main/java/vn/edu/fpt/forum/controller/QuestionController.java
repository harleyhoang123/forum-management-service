package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.CreateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.UpdateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.VoteQuestionRequest;
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

    @DeleteMapping("/{question-id}/{answer-id}")
    ResponseEntity<GeneralResponse<Object>> deleteAnswer(@PathVariable(name = "question-id") String questionId, @PathVariable(name = "answer-id") String answerId);

    @PostMapping("/{question-id}/vote")
    ResponseEntity<GeneralResponse<Object>> voteQuestion(@PathVariable(name = "question-id") String questionId, @RequestBody VoteQuestionRequest request);

    @GetMapping
    ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestion(
            @RequestParam(name = "question-id", required = false) String questionId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "title-sort-by", required = false) String titleSortBy,
            @RequestParam(name = "problem", required = false) String problem,
            @RequestParam(name = "tried-case", required = false) String triedCase,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "created-by", required = false) String createdBy,
            @RequestParam(name = "created-date-from", required = false) String createdDateFrom,
            @RequestParam(name = "created-date-to", required = false) String createdDateTo,
            @RequestParam(name = "created-date-sort-by", required = false) String createdDateSortBy,
            @RequestParam(name = "last-modified-by", required = false) String lastModifiedBy,
            @RequestParam(name = "last-modified-date-from", required = false) String lastModifiedDateFrom,
            @RequestParam(name = "last-modified-date-to", required = false) String lastModifiedDateTo,
            @RequestParam(name = "last-modified-date-sort-by", required = false) String lastModifiedDateSortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size

    );

    @GetMapping("/{question-id}")
    ResponseEntity<GeneralResponse<GetQuestionDetailResponse>> getQuestionDetail(@PathVariable(name = "question-id") String questionId);

}
