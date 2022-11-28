package vn.edu.fpt.forum.controller.impl;

import org.springframework.http.ResponseEntity;
import vn.edu.fpt.forum.controller.QuestionController;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.common.PageableResponse;
import vn.edu.fpt.forum.dto.request.question.CreateQuestionRequest;
import vn.edu.fpt.forum.dto.request.question.UpdateQuestionRequest;
import vn.edu.fpt.forum.dto.response.question.CreateQuestionResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionDetailResponse;
import vn.edu.fpt.forum.dto.response.question.GetQuestionResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 22/11/2022 - 19:38
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public class QuestionControllerImpl implements QuestionController {
    @Override
    public ResponseEntity<GeneralResponse<CreateQuestionResponse>> createQuestion(CreateQuestionRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateQuestion(String questionId, UpdateQuestionRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> closeQuestion(String questionId) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetQuestionResponse>>> getQuestion() {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<GetQuestionDetailResponse>> getQuestionDetail(String questionId) {
        return null;
    }
}
