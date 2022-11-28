package vn.edu.fpt.forum.config.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 14:16
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Configuration
public class AuthenticationFeignClientConfig {

    @Value("${feign.authentication.token}")
    private String token;

    @Bean
    public RequestInterceptor requestLoanProductInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
