package com.tq.community.service.impl;

import com.tq.community.entity.DiscussPost;
import com.tq.community.mapper.DiscussPostMapper;
import com.tq.community.service.DiscussPostService;
import com.tq.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Override
    public List<DiscussPost> selectDiscussPosts(int userid) {
        return discussPostMapper.selectDiscussPosts(userid);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记,既如果用户输入html标签让输入的标签内容成为字符串，转义的形式存入数据库
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    @Override
    public DiscussPost selectDiscussById(int id) {
        return discussPostMapper.selectDiscussById(id);
    }
}
