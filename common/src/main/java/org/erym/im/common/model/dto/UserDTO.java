package org.erym.im.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erym.im.common.model.protocol.ProtoMsg;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserDTO {
    private Long userId;
    private String userName;
    private String password;
    private String clientId;
    private String sessionId;
    String token;

    public static UserDTO fromMsg(ProtoMsg.LoginRequest info)
    {
        UserDTO user = new UserDTO();
        user.userId = Long.valueOf(new String(info.getUid()));
        user.token = new String(info.getToken());
        log.info("登录中: {}", user.toString());
        return user;
    }
}