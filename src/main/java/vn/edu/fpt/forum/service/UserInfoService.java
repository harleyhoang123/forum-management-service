package vn.edu.fpt.forum.service;

import vn.edu.fpt.forum.dto.cache.UserInfo;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/11/2022 - 08:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface UserInfoService {

    UserInfo getUserInfo(String accountId);

}
