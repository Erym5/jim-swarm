package org.erym.im.common.model.dto;

import lombok.Data;
import org.erym.im.common.model.entity.ImNode;
import org.erym.im.common.model.entity.User;

import java.util.List;

@Data
public class LoginBack
{

    List<ImNode> imNodeList;

    private String accessToken;

    private String refreshToken;

    private User user;

}