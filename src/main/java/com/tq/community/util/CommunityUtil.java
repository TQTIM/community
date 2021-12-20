package com.tq.community.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author TQ
 * @version 1.0
 * @Description 工具类
 * @create 2021-12-17 11:02
 */
public class CommunityUtil  {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密,框架自带工具类
    public static String md5(String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}