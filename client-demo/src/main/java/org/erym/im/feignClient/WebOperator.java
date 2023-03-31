package org.erym.im.feignClient;

import lombok.extern.slf4j.Slf4j;
import org.erym.im.common.constant.ServerConstants;
import org.erym.im.common.model.dto.LoginBack;
import org.erym.im.common.model.dto.ResultInfo;
import org.erym.im.common.util.JsonUtil;
import feign.Feign;
import feign.codec.StringDecoder;

@Slf4j
public class WebOperator
{

    public static LoginBack login(String userName, String password)
    {
        UserAction action = Feign.builder()
//                .decoder(new GsonDecoder())
                .decoder(new StringDecoder())
                .target(UserAction.class, ServerConstants.WEB_URL);

        String s = action.loginAction(userName, password);
        ResultInfo<LoginBack> rb = JsonUtil.jsonToPojo(s, ResultInfo.class);
        log.info(String.valueOf(rb.getData()));

        LoginBack back = JsonUtil.jsonToPojo(String.valueOf(rb.getData()), LoginBack.class);
        return back;

    }
}
