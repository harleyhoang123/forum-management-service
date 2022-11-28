package vn.edu.fpt.forum.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.forum.entity.AppConfig;

import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 05:12
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface AppConfigRepository extends MongoRepository<AppConfig, String> {

    Optional<AppConfig> findByConfigKey(String configKey);
}
