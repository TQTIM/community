package com.tq.community.service;

import com.tq.community.entity.User;

import java.util.Map;


/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 16:07
 */

public interface UserService {
    User findUserById(int userid);
    Map<String,Object> register(User user);

    /**
     *
     * @param userid 用户id
     * @param code 激活码
     * @return int
     */
    int activation(int userid,String code);

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 过期时间
     * @return Map<String,Object>
     */
    Map<String,Object> login(String username,String password,long expiredSeconds);

    void logout(String ticket);
}
