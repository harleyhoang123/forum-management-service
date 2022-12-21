package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.question.*;
import vn.edu.fpt.forum.dto.response.question.CreateQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionDetailResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 22/11/2022 - 19:45
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface QuestionService {

    CreateQuestionResponse createQuestion(CreateQuestionRequest request);

    void updateQuestion(String questionId, UpdateQuestionRequest request);

    void deleteQuestion(String questionId);

    void voteQuestion(String questionId, VoteQuestionRequest request);

    void closeQuestion(String questionId);
    PageableResponse<GetQuestionResponse> getQuestionByCondition(GetQuestionRequest request);

    PageableResponse<GetQuestionResponse> getQuestionBySearchData(GetQuestionBySearchDataRequest request);

    GetQuestionDetailResponse getQuestionDetail(String questionId);
}
