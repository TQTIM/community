package com.tq.community.controller;

import com.tq.community.annotation.LoginRequired;
import com.tq.community.entity.User;
import com.tq.community.service.UserService;
import com.tq.community.util.CommunityUtil;
import com.tq.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-22 14:50
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();//获取原始文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败:"+e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);

        }
        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8081/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        //响应图片
        response.setContentType("image/" + suffix);
        try (
                //读取服务器图片并响应到浏览器
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }

    }

    @LoginRequired
    @PostMapping("/updatePassword")
    public String updatePassword(String password_old,String password_new,Model model){
        User user = hostHolder.getUser();//因为拦截器每次请求都会根据凭证查出user并保存，渲染视图后再clear，这里可以直接获取
        int id = user.getId();
        String password=user.getPassword();
        password_old=CommunityUtil.md5(password_old+user.getSalt());
        password_new=CommunityUtil.md5(password_new+user.getSalt());
        //判断原密码是否正确
        if(StringUtils.isNotBlank(password_old)&&StringUtils.isNotBlank(password)&&password_old.equals(password)){
            //修改密码
            userService.updatePassword(id,password_new);
            //更新完应该退出重新登入
            model.addAttribute("passwordMsg","修改密码成功，请重新登入！");
            return "redirect:/logout";
        } else{
            //页面没展示错误消息值
            model.addAttribute("passwordMsg","原密码错误！");
            return "redirect:setting";
        }


    }
}
