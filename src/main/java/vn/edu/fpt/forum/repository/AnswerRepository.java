package vn.edu.fpt.forum.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.forum.constant.AnswerStatusEnum;
import vn.edu.fpt.forum.entity.Answer;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 24/11/2022 - 15:37
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    Answer findByStatus(AnswerStatusEnum status);
}
