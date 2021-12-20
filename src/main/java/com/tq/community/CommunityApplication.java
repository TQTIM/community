package com.tq.community;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//老是出现分页插件多次配置的错误，因为pagehelper会自动装配bean，这里就排除了
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
