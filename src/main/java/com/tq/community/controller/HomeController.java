package com.tq.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tq.community.entity.DiscussPost;
import com.tq.community.entity.User;
import com.tq.community.service.DiscussPostService;
import com.tq.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-15 16:52
 */
@Controller
public class HomeController {

    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    UserService userService;
    @GetMapping(value = "/index")
    public String getIndexPage(Model model,@RequestParam(defaultValue = "1",value ="pageNum") Integer pageNum){
        PageHelper.startPage(pageNum,4);
        List<DiscussPost> lists = discussPostService.selectDiscussPosts(155);//暂时固定死了，考虑是否有用户id来显示
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(lists);
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        for (DiscussPost post:pageInfo.getList()
             ) {
            Map<String,Object> map=new HashMap<>();
            map.put("post",post);
            User user = userService.findUserById(post.getUserId());
            map.put("user",user);

            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("pageInfo",pageInfo);

        return "/index";



    }
}
