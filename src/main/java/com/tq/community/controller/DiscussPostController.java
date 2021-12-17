package com.tq.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tq.community.entity.DiscussPost;
import com.tq.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 11:51
 */
@Controller
public class DiscussPostController {
    @Autowired
    DiscussPostService discussPostService;
   @RequestMapping(value = "/discussPost/{userid}",method = RequestMethod.GET)
   @ResponseBody
    public PageInfo<DiscussPost> discussPost(@PathVariable(value = "userid") int userid) {
       PageHelper.startPage(1,3);
       List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(userid);
       PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPosts);

       return pageInfo;
    }

}
