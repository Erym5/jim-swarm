package org.erym.im.service.impl;

import com.xfvape.uid.UidGenerator;
import com.xfvape.uid.impl.DefaultUidGenerator;
import org.erym.im.common.constant.CodeEnum;
import org.erym.im.feign.UserInfoService;
import org.erym.im.mapper.ChatMapper;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.Group;
import org.erym.im.common.model.entity.GroupMessage;
import org.erym.im.common.model.entity.Message;
import org.erym.im.common.model.entity.User;
import org.erym.im.common.model.vo.BoxVo;
import org.erym.im.common.model.vo.MessageVo;
import org.erym.im.common.model.vo.UserVo;
import org.erym.im.service.ChatService;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author cjt
 * @date 2021/6/20 20:20
 */
@Log4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String ONLINE_USER = "online_user";
    private static final int messageCount = 3;


    /**
     * 获取聊天记录
     */
    @Override
    public ResultInfo<List<MessageVo>> getChatById(Integer userId, Integer friendId) {
        int tableNum = (userId + friendId) % messageCount;
        List<MessageVo> messages = chatMapper.getChatById(tableNum, userId, friendId);
        //获取两者的详细信息
        User user = userInfoService.findUserById(userId).getData();
        User friend = userInfoService.findUserById(friendId).getData();
        for (MessageVo messageVo : messages) {
            if(messageVo.getSender().equals(userId)){
                messageVo.setUserName(user.getUserName());
                messageVo.setAvatar(user.getAvatar());
            }else{
                messageVo.setUserName(friend.getUserName());
                messageVo.setAvatar(friend.getAvatar());
            }
        }
        return ResultInfo.success(CodeEnum.SUCCESS, messages);
    }


    /**
     * 保存聊天记录
     * @param message
     * @return
     */
    @Override
    public ResultInfo<Message> saveMessage(Message message) {
        message.setId(Long.valueOf(1)); //生成分布式id
        int tableNum = (int) ((message.getSender() + message.getReceiver()) % messageCount);
        chatMapper.saveMessage(tableNum, message);
        return ResultInfo.success(CodeEnum.SUCCESS, message);
    }

    /**
     * 删除聊天记录
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public ResultInfo<?> deleteChat(Integer userId, Integer friendId) {
        int tableNum = (userId + friendId) % messageCount;
        chatMapper.deleteChat(tableNum, userId, friendId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 获取所有聊天框
     * @param userId
     * @return
     */
    @Override
    public ResultInfo<List<BoxVo>> getAllChatBox(Integer userId) {
        List<BoxVo> boxVoList = new LinkedList<>();
        //获取私聊
        List<UserVo> friendList = userInfoService.findAllFriend(userId).getData();
        for(UserVo userVo : friendList){
            Boolean status = redisTemplate.opsForSet().isMember(ONLINE_USER,
                    String.valueOf(userVo.getUser().getUserId()));
            userVo.setStatus(Boolean.TRUE.equals(status));
            BoxVo boxVo = new BoxVo();
            boxVo.setUserVo(userVo);
            boxVo.setType(false);
            boxVoList.add(boxVo);
        }
        return ResultInfo.success(CodeEnum.SUCCESS, boxVoList);
    }

}
