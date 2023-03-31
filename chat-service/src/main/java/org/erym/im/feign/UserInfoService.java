package org.erym.im.feign;

import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.User;
import org.erym.im.common.model.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjt
 * @date 2022/6/9 22:18
 */
@FeignClient(value = "user-info")
public interface UserInfoService {

    @GetMapping("/user-info/user/findUserById")
    ResultInfo<User> findUserById(@RequestParam("userId") Integer userId);

    @GetMapping("/user-info/friend/findAllFriend")
    ResultInfo<List<UserVo>> findAllFriend(@RequestParam("userId") Integer userId);

}
