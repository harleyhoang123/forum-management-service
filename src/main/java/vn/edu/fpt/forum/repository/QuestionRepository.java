package vn.edu.fpt.forum.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.forum.entity.Question;

import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 04:46
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

}
