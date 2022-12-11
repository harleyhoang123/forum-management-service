package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.request.answer.CreateAnswerRequest;
import vn.edu.fpt.forum.dto.request.answer.UpdateAnswerRequest;
import vn.edu.fpt.forum.dto.response.answer.CreateAnswerResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 11:13
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface AnswerService {

    CreateAnswerResponse createAnswer(String questionId, CreateAnswerRequest request);

    void updateAnswer(String answerId, UpdateAnswerRequest request);

    void deleteAnswer(String questionId, String answerId);

    void voteAnswer(String answerId, Boolean isIncrease);
}
