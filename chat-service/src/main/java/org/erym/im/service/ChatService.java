package org.erym.im.service;

import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.Group;
import org.erym.im.common.model.entity.GroupMessage;
import org.erym.im.common.model.entity.Message;
import org.erym.im.common.model.vo.BoxVo;
import org.erym.im.common.model.vo.MessageVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author cjt
 * @date 2021/6/20 20:20
 */
public interface ChatService {

    ResultInfo<List<MessageVo>> getChatById(Integer userId, Integer friendId);

    ResultInfo<Message> saveMessage(Message message);

    ResultInfo<?> deleteChat(Integer userId, Integer friendId);

    ResultInfo<List<BoxVo>> getAllChatBox(Integer userId);
}
