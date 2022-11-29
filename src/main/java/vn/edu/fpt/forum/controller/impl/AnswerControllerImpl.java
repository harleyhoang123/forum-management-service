package vn.edu.fpt.forum.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.controller.AnswerController;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;
import vn.edu.fpt.forum.factory.ResponseFactory;
import vn.edu.fpt.forum.service.AnswerService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 20:35
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
public class AnswerControllerImpl implements AnswerController {

    private final ResponseFactory responseFactory;
    private final AnswerService answerService;

    @Override
    public ResponseEntity<GeneralResponse<CreateAnswerResponse>> createAnswer(CreateAnswerRequest request) {
        return responseFactory.response(answerService.createAnswer(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateAnswer(String answerId, UpdateAnswerRequest request) {
        answerService.updateAnswer(answerId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteAnswer(String answerId) {
        answerService.deleteAnswer(answerId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
