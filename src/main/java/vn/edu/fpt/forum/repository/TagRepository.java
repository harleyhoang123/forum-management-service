package vn.edu.fpt.forum.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.forum.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 04:51
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    List<Tag> findAllByTagNameIn(List<String> tagNames);
    Optional<Tag> findByTagNameAndOwnerBy(String tagName, String ownerBy);
}
