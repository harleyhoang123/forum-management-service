package vn.edu.fpt.forum.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.controller.QuestionController;
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
import vn.edu.fpt.forum.factory.ResponseFactory;
import vn.edu.fpt.forum.service.AnswerService;
import vn.edu.fpt.forum.service.CommentService;
import vn.edu.fpt.forum.service.QuestionService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 22/11/2022 - 19:38
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class QuestionControllerImpl implements QuestionController {

    private final QuestionService questionService;
    private final ResponseFactory responseFactory;
    private final AnswerService answerService;
    private final CommentService commentService;

    @Override
    public ResponseEntity<GeneralResponse<CreateQuestionResponse>> createQuestion(CreateQuestionRequest request) {
        return responseFactory.response(questionService.createQuestion(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<AddCommentToQuestionResponse>> addCommentToQuestion(String questionId, AddCommentToQuestionRequest request) {
        return responseFactory.response(commentService.addCommentToQuestion(questionId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<CreateAnswerResponse>> addAnswerToQuestion(String questionId, CreateAnswerRequest request) {
        return responseFactory.response(answerService.createAnswer(questionId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateQuestion(String questionId, UpdateQuestionRequest request) {
        questionService.updateQuestion(questionId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> closeQuestion(String questionId) {
        questionService.closeQuestion(questionId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestion() {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<GetQuestionDetailResponse>> getQuestionDetail(String questionId) {
        return responseFactory.response(questionService.getQuestionDetail(questionId));
    }
}
