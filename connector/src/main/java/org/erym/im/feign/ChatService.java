package org.erym.im.feign;

import org.erym.im.feign.fallback.ChatServiceFallback;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.model.entity.GroupMessage;
import org.erym.im.common.model.entity.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author cjt
 * @date 2022/6/10 23:46
 */
@FeignClient(value = "chat-service", fallback = ChatServiceFallback.class)
public interface ChatService {

    @PostMapping("/chat/saveMessage")
    ResultInfo<Message> saveMessage(@RequestBody Message message);

    @PostMapping("/chat/saveGroupMessage")
    ResultInfo<GroupMessage> saveGroupMessage(@RequestBody GroupMessage groupMessage);
}
