package com.tq.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tq.community.entity.DiscussPost;
import com.tq.community.entity.User;
import com.tq.community.service.DiscussPostService;
import com.tq.community.service.UserService;
import com.tq.community.util.CommunityUtil;
import com.tq.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 11:51
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    UserService userService;
    @Autowired
    private HostHolder hostHolder;

   @RequestMapping(value = "/{userid}",method = RequestMethod.GET)
   @ResponseBody
    public PageInfo<DiscussPost> discussPost(@PathVariable(value = "userid") int userid) {
       PageHelper.startPage(1,3);
       List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(userid);
       PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPosts);

       return pageInfo;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");

    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){
       //帖子
        DiscussPost Post = discussPostService.selectDiscussById(discussPostId);
        model.addAttribute("post",Post);
        //作者
        User user = userService.findUserById(Post.getUserId());
        model.addAttribute("user",user);


        return "/site/discuss-detail";

    }



}
