package com.tq.community.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author TQ
 * @version 1.0
 * @Description
 * @create 2021-12-20 15:44
 */
@Getter
@Setter
@ToString
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
