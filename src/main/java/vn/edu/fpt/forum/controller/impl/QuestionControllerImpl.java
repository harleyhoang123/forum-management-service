package vn.edu.fpt.forum.controller.impl;

import com.amazonaws.services.dynamodbv2.model.Get;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.s;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.controller.QuestionController;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.common.SortableRequest;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.comment.AddCommentToQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.CreateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.GetQuestionRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<GeneralResponse<Object>> voteQuestion(String questionId) {
        questionService.voteQuestion(questionId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestion(String questionId,
                                                                                              String title,
                                                                                              String titleSortBy,
                                                                                              String problem,
                                                                                              String triedCase,
                                                                                              String tag,
                                                                                              String status,
                                                                                              String createdBy,
                                                                                              String createdDateFrom,
                                                                                              String createdDateTo,
                                                                                              String createdDateSortBy,
                                                                                              String lastModifiedBy,
                                                                                              String lastModifiedDateFrom,
                                                                                              String lastModifiedDateTo,
                                                                                              String lastModifiedDateSortBy,
                                                                                              Integer page,
                                                                                              Integer size) {
        List<SortableRequest> sortableRequests = new ArrayList<>();
        if(Objects.nonNull(titleSortBy)){
            sortableRequests.add(new SortableRequest("title", titleSortBy));
        }
        if(Objects.nonNull(createdDateSortBy)){
            sortableRequests.add(new SortableRequest("created_date", createdDateSortBy));
        }
        if(Objects.nonNull(lastModifiedDateSortBy)){
            sortableRequests.add(new SortableRequest("last_modified_date", lastModifiedDateSortBy));
        }
        GetQuestionRequest request = GetQuestionRequest.builder()
                .questionId(questionId)
                .title(title)
                .problem(problem)
                .triedCase(triedCase)
                .status(status)
                .tag(tag)
                .createdBy(createdBy)
                .createdDateFrom(createdDateFrom)
                .createdDateTo(createdDateTo)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedDateFrom(lastModifiedDateFrom)
                .lastModifiedDateTo(lastModifiedDateTo)
                .page(page)
                .size(size)
                .sortBy(sortableRequests)
                .build();
        return responseFactory.response(questionService.getQuestionByCondition(request));
    }

//    @Override
//    public ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestionBySearchData(){
//
//    }

    @Override
    public ResponseEntity<GeneralResponse<GetQuestionDetailResponse>> getQuestionDetail(String questionId) {
        return responseFactory.response(questionService.getQuestionDetail(questionId));
    }
}
