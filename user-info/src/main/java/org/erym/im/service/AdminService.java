package org.erym.im.service;

import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.vo.UserVo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author cjt
 * @date 2022/11/2 22:13
 */
public interface AdminService {

    ResultInfo<JSONObject> login(String name, String password) throws Exception;

    ResultInfo<JSONObject> refreshAccessToken(String refreshToken) throws Exception;

    ResultInfo<List<UserVo>> getAllUser();

    ResultInfo<String> banUser(Integer userId, String daysStr);

    ResultInfo<String> unBan(Integer userId);

}
