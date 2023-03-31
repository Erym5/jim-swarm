package org.erym.im.feign;

import org.erym.im.feign.fallback.UserInfoServiceFallback;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.Group;
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
@FeignClient(value = "user-info", fallback = UserInfoServiceFallback.class)
public interface UserInfoService {

    @GetMapping("/user-info/user/findUserById")
    ResultInfo<User> findUserById(@RequestParam("userId") Long userId);

    @GetMapping("/user-info/user/getGroupById")
    ResultInfo<Group> getGroupById(@RequestParam("groupId") Integer groupId);

    @GetMapping("/user-info/user/getAllGroup")
    ResultInfo<List<Group>> getAllGroup(@RequestParam("userId") Integer userId);

    @GetMapping("/user-info/friend/findAllFriend")
    ResultInfo<List<UserVo>> findAllFriend(@RequestParam("userId") Integer userId);

    @GetMapping("/user-info/user/getGroupUser")
    ResultInfo<List<Integer>> getGroupUser(@RequestParam("groupId") Integer groupId);
}
