package com.tq.community.interceptor;

import com.tq.community.annotation.LoginRequired;
import com.tq.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TQ
 * @version 1.0
 * @Description 拦截未登入时直接访问敏感路径地址的请求
 * @create 2021-12-23 13:41
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(handler instanceof HandlerMethod){//拦截到的类型是否属于方法类型
           HandlerMethod handlerMethod = (HandlerMethod) handler;
           LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);//从方法中取指定类型的注解
           if(loginRequired != null && hostHolder.getUser() == null){
               //未登入状态访问，拦截
               response.sendRedirect(request.getContextPath() + "/login");
               return false;
           }
       }
        return true;
    }
}
