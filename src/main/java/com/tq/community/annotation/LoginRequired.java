package com.tq.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TQ
 * @version 1.0
 * @Description 自定义注解，作为是否登入的拦截判断标识
 * @create 2021-12-23 13:43
 */
@Target(ElementType.METHOD) //元注解，作用再方法上
@Retention(RetentionPolicy.RUNTIME)//元注解，运行时生效
public @interface LoginRequired {
    //作为标识，加在controller敏感访问路径上
}
