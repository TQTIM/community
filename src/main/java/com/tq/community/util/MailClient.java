package com.tq.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-16 17:31
 */
@Component
public class MailClient {
    Logger logger= LoggerFactory.getLogger(MailClient.class);
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to,String title,String content){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);//后面参数为是否要上传附件
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content,true);
            //上传文件
            helper.addAttachment("1.jpg",new File("C:\\Users\\TQ\\Pictures\\Saved Pictures\\其.png"));//可写多个
            mailSender.send(helper.getMimeMessage());

        } catch (MessagingException e) {
            logger.error("发送邮件失败",e.getMessage());
        }

    }
}
