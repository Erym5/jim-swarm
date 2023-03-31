package org.erym.im.feignClient;

import feign.Param;
import feign.RequestLine;
import org.erym.im.common.model.dto.LoginBack;
import org.erym.im.common.model.dto.ResultInfo;

/**
 * 远程接口的本地代理
 * Created by 尼恩 at 疯狂创客圈
 */
public interface UserAction
{

    /**
     * 登录代理
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @RequestLine("POST /user-info/user/login/{username}/{password}")
    public String loginAction(
            @Param("username") String username,
            @Param("password") String password);


    /**
     * 获取用户信息代理
     *
     * @param userid 用户id
     * @return 用户信息
     */
    @RequestLine("GET /{userid}")
    public String getById(@Param("userid") Integer userid);


}
