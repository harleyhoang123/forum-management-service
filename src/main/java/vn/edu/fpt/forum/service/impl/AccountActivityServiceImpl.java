package vn.edu.fpt.forum.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.forum.dto.event.CreateAccountActivityEvent;
import vn.edu.fpt.forum.entity.AccountActivity;
import vn.edu.fpt.forum.exception.BusinessException;
import vn.edu.fpt.forum.repository.AccountActivityRepository;
import vn.edu.fpt.forum.service.AccountActivityService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 10:38
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivityServiceImpl implements AccountActivityService {

    private final AccountActivityRepository accountActivityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void createAccountActivity(String value){
        try {
            CreateAccountActivityEvent createAccountActivityEvent = objectMapper.readValue(value, CreateAccountActivityEvent.class);
            AccountActivity accountActivity = AccountActivity.builder()
                    .accountId(createAccountActivityEvent.getAccountId())
                    .build();

            accountActivityRepository.save(accountActivity);

        }catch (Exception ex){
            throw new BusinessException("Can't create account activity from authentication service: " + ex.getMessage());
        }
    }
}
