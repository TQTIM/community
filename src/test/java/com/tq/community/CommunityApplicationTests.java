package com.tq.community;

import com.tq.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    MailClient mailClient;
    @Test
    void contextLoads() {
    //    mailClient.sendMail("1330875414@qq.com","邮箱测试","<b style='color:red'>测试邮件。。来自-其</b>");
    }

}
