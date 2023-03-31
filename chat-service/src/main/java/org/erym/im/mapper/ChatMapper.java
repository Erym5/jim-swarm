package org.erym.im.mapper;

import org.erym.im.common.model.entity.Group;
import org.erym.im.common.model.entity.Message;
import org.erym.im.common.model.vo.MessageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/20 20:09
 */
@Repository
public interface ChatMapper {

    List<MessageVo> getChatById(@Param("tableNum") int tableNum, @Param("userId") Integer userId,
                                @Param("friendId") Integer friendId);


    void saveMessage(@Param("tableNum") int tableNum, @Param("message") Message message);

    void deleteChat(@Param("tableNum") int tableNum, @Param("userId") Integer userId, @Param("friendId") Integer friendId);
}
