package org.erym.im.controller;

import org.erym.im.common.model.dto.LoginBack;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.util.JsonUtil;
import org.erym.im.service.UserService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author cjt
 * @date 2022/6/6 23:31
 */
@RestController
@RequestMapping("/user-info/user")
public class UserController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户查找")
    @GetMapping("/loadUserByUsername")
    public UserDTO loadUserByUsername(@RequestParam("userName") String userName) throws Exception {
        return userService.loadloadUserByUsername(userName);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login/{userName}/{password}")
    public String login(@PathVariable("userName")String userName, @PathVariable("password")String password) throws Exception {
//        return ResultInfo.success(200, "OK", "OK");
        String s = JsonUtil.pojoToJson(userService.login(userName, password));
        return s;
    }

    @ApiOperation(value = "刷新token")
    @PostMapping("/refresh-tokens")
    public ResultInfo<JSONObject> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        return userService.refreshAccessToken(refreshToken);
    }

    @GetMapping("/isBan")
    public ResultInfo<String> isBan(Integer userId) {
        return userService.isBan(userId);
    }
}
