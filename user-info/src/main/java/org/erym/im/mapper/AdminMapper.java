package org.erym.im.mapper;

import org.erym.im.common.model.entity.Admin;
import org.erym.im.common.model.entity.Ban;
import org.erym.im.common.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cjt
 * @date 2022/11/2 22:04
 */
@Repository
public interface AdminMapper {

    Admin login(@Param("name") String name, @Param("password") String password);

    Admin getAdminById(@Param("id") Long id);

    List<User> getAllUser();

    void insertBan(@Param("ban") Ban ban);

}
