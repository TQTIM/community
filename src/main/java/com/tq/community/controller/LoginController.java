package com.tq.community.controller;

import com.google.code.kaptcha.Producer;
import com.tq.community.entity.MailConstant;
import com.tq.community.entity.User;
import com.tq.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-17 11:23
 */
@Controller
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    Producer kaptchaProducer;
    private static final Logger logger=LoggerFactory.getLogger(LoginController.class);
    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }

    @GetMapping("/getKaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入seaaion中
        session.setAttribute("kaptcha",text);

        //将图片输出到浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }

    }
    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("userMsg", map.get("userMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    /**
     * 邮箱激活
     */
    // http://localhost:8081/community/activation/101/code
    @GetMapping("/activation/{userid}/{code}")
    public String activation(Model model, @PathVariable("userid") int userid,@PathVariable("code") String code) {
        int result = userService.activation(userid, code);
        if(result== MailConstant.ACTIVATION_SUCCESS.getCode()){
            model.addAttribute("msg",MailConstant.ACTIVATION_SUCCESS.getMsg());
            model.addAttribute("target","/login");
        }else if(result==MailConstant.ACTIVATION_REPEAT.getCode()){
            model.addAttribute("msg",MailConstant.ACTIVATION_REPEAT.getMsg());
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg",MailConstant.ACTIVATION_FAILURE.getMsg());
            model.addAttribute("target","/index");
        }

        return "/site/operate-result";
    }

}
