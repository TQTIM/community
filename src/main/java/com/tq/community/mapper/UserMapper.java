package com.tq.community.mapper;

import com.tq.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 16:13
 */
@Mapper
@Repository
public interface UserMapper {
    User findUserById(int userid);
    User selectByName(String username);
    User selectByEmail(String email);
    int insertUser(User user);
    int updateStatus(@Param("id") int userid);
}
