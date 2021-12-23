package com.tq.community.service.impl;

import com.tq.community.entity.LoginTicket;
import com.tq.community.entity.MailConstant;
import com.tq.community.entity.User;
import com.tq.community.mapper.LoginTicketMapper;
import com.tq.community.mapper.UserMapper;
import com.tq.community.service.UserService;
import com.tq.community.util.CommunityUtil;
import com.tq.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    @Autowired
    LoginTicketMapper loginTicketMapper;
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
        if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            map.put("userMsg","账号或密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
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

    @Override
    public Map<String, Object> login(String username, String password, long expiredSeconds) {
        Map<String,Object> map=new HashMap<>();

        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","该账号未激活！");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        //生成登入凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);//0-有效，1-无效
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {

        return loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public int updateHeader(int id, String headerUrl) {
        return userMapper.updateHeader(id, headerUrl);
    }

    @Override
    public int updatePassword(int id, String password) {
        return userMapper.updatePassword(id,password);
    }
}
