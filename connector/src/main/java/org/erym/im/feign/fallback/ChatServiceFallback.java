package org.erym.im.feign.fallback;

import org.erym.im.feign.ChatService;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.GroupMessage;
import org.erym.im.common.model.entity.Message;
import org.springframework.stereotype.Component;

/**
 * @author cjt
 * @date 2022/6/20 17:03
 */
@Component
public class ChatServiceFallback implements ChatService {
    @Override
    public ResultInfo<Message> saveMessage(Message message) {
        return new ResultInfo<>(500, "远程调用失败");
    }

    @Override
    public ResultInfo<GroupMessage> saveGroupMessage(GroupMessage groupMessage) {
        return new ResultInfo<>(500, "远程调用失败");
    }
}
