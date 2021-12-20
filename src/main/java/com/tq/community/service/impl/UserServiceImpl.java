package com.tq.community.service.impl;

import com.tq.community.entity.MailConstant;
import com.tq.community.entity.User;
import com.tq.community.mapper.UserMapper;
import com.tq.community.service.UserService;
import com.tq.community.util.CommunityUtil;
import com.tq.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    MailClient mailClient;
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Override
    public User findUserById(int userid) {

        return userMapper.findUserById(userid);
    }

    @Override
    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        if(user==null){
            throw new IllegalArgumentException("用户参数不能为空");
        }
        if(StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())){
            map.put("userMsg","账号或密码不能为空！");
            return map;
        }
        if(StringUtils.isEmpty(user.getEmail())){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }
        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u !=null){
            map.put("userMsg","该账号已存在！");
            return map;
        }
             u = userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","该邮箱已被注册!");
            return  map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        //激活码
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));//随机头像
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8081/community/activation/101/code
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        //template模板引擎将值填入指定html，因为这里不是controller层不会被mvc调用，邮件以html格式发送
        String content=templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }

    @Override
    public int activation(int userid, String code) {
        User user = userMapper.findUserById(userid);
        if(user.getStatus()==1){
            return MailConstant.ACTIVATION_REPEAT.getCode();
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userid);
            return MailConstant.ACTIVATION_SUCCESS.getCode();
        }else {
            return MailConstant.ACTIVATION_FAILURE.getCode();
        }

    }
}
