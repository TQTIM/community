package com.tq.community;

import com.tq.community.util.MailClient;
import com.tq.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    MailClient mailClient;
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    void contextLoads() {
    //    mailClient.sendMail("1330875414@qq.com","邮箱测试","<b style='color:red'>测试邮件。。来自-其</b>");
    }

    //过滤敏感词测试
    @Test
    void sensitiveWords() {
        String text="网络非法之地，可以赌博，也可以嫖娼，还可以吸毒呢，各种可以！";
        String text2="网络非法之地，可以赌@博，也可以嫖$娼，还可以❤吸❤毒呢，各种可以！";
        String result = sensitiveFilter.filter(text);
        String result2 = sensitiveFilter.filter(text2);
        System.out.println(result);
        System.out.println(result2);
    }

}
