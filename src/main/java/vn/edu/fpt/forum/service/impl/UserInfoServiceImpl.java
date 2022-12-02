package vn.edu.fpt.forum.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.dto.cache.UserInfo;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.service.UserInfoService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 08:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public UserInfo getUserInfo(String accountId) {
        try {
            String userInfoStr = redisTemplate.opsForValue().get(String.format("userinfo:%s", accountId));
            return objectMapper.readValue(userInfoStr, UserInfo.class);
        }catch (Exception ex){
            log.info("Can't get userinfo in redis: {}", ex.getMessage());
            return null;
        }
    }
}
