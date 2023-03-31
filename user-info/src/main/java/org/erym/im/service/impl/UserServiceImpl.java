package org.erym.im.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.erym.im.common.constant.CodeEnum;
import org.erym.im.common.constant.ServerConstants;
import org.erym.im.common.exception.ConditionException;
import org.erym.im.common.model.dto.LoginBack;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.model.entity.ImNode;
import org.erym.im.common.model.entity.User;
import org.erym.im.common.util.JsonUtil;
import org.erym.im.common.util.TokenUtil;
import org.erym.im.mapper.UserMapper;
import org.erym.im.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cjt
 * @date 2021/6/19 16:48
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String BAN_USER = "ban_user:";

    @Override
    public UserDTO loadloadUserByUsername(@RequestParam("userName") String userName){
        log.info("user-info");
        User user = userMapper.loadUserByUsername(userName);
        if (user != null) {
            UserDTO userDto = new UserDTO();
            log.info("User:"+user);
            BeanUtil.copyProperties(user,userDto);
            log.info("UserDTO:"+userDto);
            return userDto;
        }
        return null;
    }

    /**
     * 登录
     */
    @Override
    public ResultInfo<LoginBack> login(String userName, String password) throws Exception {
        if (userName.length() == 0 || password.length() == 0){
            return ResultInfo.error(400, "请填写完整信息");
        }
        User user = userMapper.login(userName, password);
        if (user == null){
            return ResultInfo.error(400, "用户名或密码错误");
        }
        Long userId = Long.valueOf(user.getUserId());
        String accessToken = TokenUtil.generateAccessToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);

        NamingService namingService = NamingFactory.createNamingService(ServerConstants.nacosServer);
        List<Instance> list = namingService.getAllInstances(ServerConstants.nettyName);
        List<ImNode> imNodeList = new ArrayList<>();
        for (Instance instance : list) {
            ImNode imNode = new ImNode();
            imNode.setHost(instance.getIp());
            imNode.setPort(instance.getPort());
            Map<String, String> metadata = instance.getMetadata();
            log.info(String.valueOf(Integer.valueOf(metadata.get("balance"))));
//            imNode.setBalance(Integer.valueOf(metadata.get("balance")));
            imNodeList.add(imNode);
        }

        LoginBack loginBack = new LoginBack();
        loginBack.setAccessToken(accessToken);
        loginBack.setRefreshToken(refreshToken);
        loginBack.setUser(user);
        loginBack.setImNodeList(imNodeList);

        return ResultInfo.success(CodeEnum.SUCCESS, loginBack);
    }

    @Override
    public ResultInfo<JSONObject> refreshAccessToken(String refreshToken) throws Exception {
        Long userId = TokenUtil.verifyToken(refreshToken);
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new ConditionException("认证token失败");
        }
        //更新双token
        String accessToken = TokenUtil.generateAccessToken(userId);
        String newRefreshToken = TokenUtil.generateRefreshToken(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accessToken", accessToken);
        jsonObject.put("refreshToken", newRefreshToken);
        return ResultInfo.success(CodeEnum.SUCCESS, jsonObject);
    }

    @Override
    public ResultInfo<User> findUserById(Long userId) {
        User user = userMapper.getUserById(userId);
        return ResultInfo.success(CodeEnum.SUCCESS, user);
    }

    @Override
    public ResultInfo<List<User>> getUserByIdList(List<Integer> userIdList) {
        List<User> userList = userMapper.getUserByIdList(userIdList);
        return ResultInfo.success(CodeEnum.SUCCESS, userList);
    }

    @Override
    public ResultInfo<String> isBan(Integer userId) {
        Boolean result = redisTemplate.hasKey(BAN_USER + userId);
        if (Boolean.TRUE.equals(result)) {
            return ResultInfo.error(CodeEnum.FORBIDDEN);
        } else {
            return ResultInfo.success(CodeEnum.SUCCESS);
        }
    }
}
