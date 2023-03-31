package org.erym.im.mapper;

import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/19 16:48
 */
@Repository
public interface UserMapper {
    User loadUserByUsername(@Param("userName") String userName);

    User login(@Param("userName") String userName, @Param("password") String password);

    User getUserById(Long userId);

    List<User> getUserByIdList(@Param("userIdList") List<Integer> userIdList);

    User getUserByName(String userName);

}
