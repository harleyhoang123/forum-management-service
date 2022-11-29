package vn.edu.fpt.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.response.answer.AddAnswerToQuestionResponse;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 08:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/answers")
public interface AnswerController {

    @PostMapping("/answers")
    ResponseEntity<GeneralResponse<CreateAnswerResponse>> createAnswer(@RequestBody CreateAnswerRequest request);

    @PutMapping("/{answer-id}")
    ResponseEntity<GeneralResponse<Object>> updateAnswer(@PathVariable(name = "answer-id") String answerId, @RequestBody UpdateAnswerRequest request);

    @DeleteMapping("/{answer-id}")
    ResponseEntity<GeneralResponse<Object>> deleteAnswer(@PathVariable(name = "answer-id") String answerId);

}
