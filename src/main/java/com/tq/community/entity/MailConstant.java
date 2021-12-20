package com.tq.community.entity;

import lombok.Getter;

/**
 * @author TQ
 * @version 1.0
 * @Description 邮件激活状态
 * @create 2021-12-17 16:22
 */
@Getter
public enum MailConstant {
    ACTIVATION_SUCCESS(0,"激活成功"),
    ACTIVATION_REPEAT(1,"重复激活"),
    ACTIVATION_FAILURE(2,"激活失败");

    private int code;
    private String msg;

    MailConstant(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

}
