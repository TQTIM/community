package com.tq.community.service;

import com.tq.community.entity.User;


/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 16:07
 */

public interface UserService {
    User findUserById(int userid);
}
