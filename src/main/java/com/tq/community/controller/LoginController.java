package com.tq.community.controller;

import com.google.code.kaptcha.Producer;
import com.tq.community.entity.MailConstant;
import com.tq.community.entity.User;
import com.tq.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
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

    private static final long DEFAULT_EXPIRED_SECONDS=3600*12L;
    private static final long REMEMBER_EXPIRED_SECONDS=3600*24*100L;
    @Value("${server.servlet.context-path}")
    private String contextPath;

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


    @PostMapping("/login")
   //如果是对象传入会直接加到Model中在放入request域，零散参数是在request域中。自定义的pojo类型作为方法参数时，相当于默认加上了@ModelAttribute注解，默认key值为类名（首字母小写）
    public String login(String username,String password,String code,boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response){
        //检查验证码
        String kaptcha = (String)session.getAttribute("kaptcha");
        if(StringUtils.isEmpty(kaptcha)||StringUtils.isEmpty(code)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确！");
            return "/site/login";
        }

        //检查账号密码
        long expiredSeconds=rememberme ? REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password,expiredSeconds);
        if(map.containsKey("ticket")){
            //将凭证写入到cookie中发送给浏览器
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);//路径写整个项目，@value注入
            cookie.setMaxAge((int)expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index"; //事已经做完，重定向过去
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";

        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
