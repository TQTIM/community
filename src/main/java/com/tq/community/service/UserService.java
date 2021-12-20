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
    int activation(int userid,String code);
}
