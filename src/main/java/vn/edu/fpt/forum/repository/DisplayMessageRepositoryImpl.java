package vn.edu.fpt.forum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.forum.constant.ResponseStatusEnum;
import vn.edu.fpt.forum.entity.DisplayMessage;
import vn.edu.fpt.forum.exception.BusinessException;

import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/11/2022 - 07:58
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
@RequiredArgsConstructor
public class DisplayMessageRepositoryImpl implements DisplayMessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<DisplayMessage> findByCodeAndLanguage(String code, String language) {
        try {
            DisplayMessage displayMessage =  (DisplayMessage) redisTemplate.opsForValue().get(String.format("%s:%s", code, language));
            return Optional.of(displayMessage);
        }catch (Exception ex){
            return Optional.empty();
        }
    }
}
