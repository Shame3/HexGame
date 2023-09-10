package com.hex.entity;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("====================进入拦截器====================");

        Long userId= Long.valueOf(-1),adminId= Long.valueOf(-1);
        if (request.getSession().getAttribute("userId")!=null)
            userId = (Long) request.getSession().getAttribute("userId");
        if (request.getSession().getAttribute("adminId")!=null)
            adminId = (Long) request.getSession().getAttribute("adminId");
        System.out.println(userId);
        if (userId == -1&&adminId==-1){
            response.sendRedirect("/login/error");       //检测到用户未登录时，跳转到error界面
            return  false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}