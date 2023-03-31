package org.erym.im.controller;

import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.vo.MessageVo;
import org.erym.im.common.model.entity.Message;
import org.erym.im.common.model.vo.BoxVo;
import org.erym.im.common.model.vo.MessageVo;
import org.erym.im.service.ChatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author cjt
 * @date 2022/5/8 19:56
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @ApiOperation("获取聊天记录")
    @GetMapping("/getChatById")
    public ResultInfo<List<MessageVo>> getChatById(Integer userId, Integer friendId) {
        return chatService.getChatById(userId, friendId);
    }

    @ApiOperation("删除聊天记录")
    @PostMapping("/deleteChat")
    public ResultInfo<?> deleteChat(Integer userId, Integer friendId) {
        return chatService.deleteChat(userId, friendId);
    }

    @ApiOperation("获取所有聊天框")
    @GetMapping("/getAllChatBox")
    public ResultInfo<List<BoxVo>> getAllChatBox(Integer userId) {
        return chatService.getAllChatBox(userId);
    }


    @PostMapping("/saveMessage")
    public ResultInfo<Message> saveMessage(@RequestBody Message message) {
        return chatService.saveMessage(message);
    }

}