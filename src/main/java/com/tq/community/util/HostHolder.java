package com.tq.community.util;

import com.tq.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息,用于代替session对象.
 * 用的ThreadLocal，保证多线程下user对象是此时登入的用户，
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
