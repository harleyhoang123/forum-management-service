package vn.edu.fpt.forum.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.service.AccountActivityService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 09:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountForumActivityConsumer extends Consumer{

    private final AccountActivityService accountActivityService;

    @Override
    @KafkaListener(id = "createAccountForumActivityConsumer", topics = "flab.forum.account_activity", groupId = "forum_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        accountActivityService.createAccountActivity(value);
    }
}
