package com.tq.community.service.impl;

import com.tq.community.entity.User;
import com.tq.community.mapper.UserMapper;
import com.tq.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 16:12
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User findUserById(int userid) {

        return userMapper.findUserById(userid);
    }
}
