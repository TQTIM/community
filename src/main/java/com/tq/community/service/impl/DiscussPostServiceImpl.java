package com.tq.community.service.impl;

import com.tq.community.mapper.DiscussPostMapper;
import com.tq.community.entity.DiscussPost;
import com.tq.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 11:10
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Override
    public List<DiscussPost> selectDiscussPosts(int userid) {
        return discussPostMapper.selectDiscussPosts(userid);
    }
}
