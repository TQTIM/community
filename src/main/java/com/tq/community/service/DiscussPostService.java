package com.tq.community.service;

import com.tq.community.entity.DiscussPost;

import java.util.List;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 11:08
 */
public interface DiscussPostService {
    List<DiscussPost> selectDiscussPosts(int userid);

    //增加帖子
   int addDiscussPost(DiscussPost post);

   //帖子详情
    DiscussPost selectDiscussById(int id);

}
