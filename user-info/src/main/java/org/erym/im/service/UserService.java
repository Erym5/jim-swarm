package org.erym.im.service;

import org.erym.im.common.model.dto.LoginBack;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.model.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/19 16:47
 */
public interface UserService {
    UserDTO loadloadUserByUsername(@RequestParam("userName") String userName);

    ResultInfo<LoginBack> login(String userName, String password) throws Exception;

    ResultInfo<JSONObject> refreshAccessToken(String refreshToken) throws Exception;

    ResultInfo<User> findUserById(Long userId);

    ResultInfo<List<User>> getUserByIdList(List<Integer> userIdList);

    ResultInfo<String> isBan(Integer userId);
}
