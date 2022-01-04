package com.tq.community.mapper;

import com.tq.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 10:48
 */
@Mapper
@Repository
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(@Param(value = "userid") int userid);

    //发帖
    int insertDiscussPost(DiscussPost discussPost);
    //帖子详情
    DiscussPost selectDiscussById(int id);

}
