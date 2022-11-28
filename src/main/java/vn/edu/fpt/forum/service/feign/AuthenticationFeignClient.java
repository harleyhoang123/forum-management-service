package vn.edu.fpt.forum.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.fpt.forum.config.feign.AuthenticationFeignClientConfig;
import vn.edu.fpt.forum.dto.common.GeneralResponse;
import vn.edu.fpt.forum.dto.feign.response.GetAccountIdByUsernameResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 13:19
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@FeignClient(name = "authentication-feign-client", url = "${feign.authentication.url}", configuration = AuthenticationFeignClientConfig.class)
public interface AuthenticationFeignClient {

    @GetMapping(value = "/accounts/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GeneralResponse<GetAccountIdByUsernameResponse>> getAccountIdsByUsername(@PathVariable(name = "username") String username);
}
