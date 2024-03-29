package com.tq.community.mapper;

import com.tq.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author TQ
 * @version 1.0
 * @Description 登入凭证
 * @create 2021-12-20 15:45
 */
@Mapper
@Repository
public interface LoginTicketMapper {

    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id") //主键自增，并回填
    int insertLoginTicket(LoginTicket ticket);

    @Select({"select id,user_id,ticket,status,expired from login_ticket where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update login_ticket set status=#{status} where ticket=#{ticket}"})
    int updateStatus(String ticket,int status);



}
