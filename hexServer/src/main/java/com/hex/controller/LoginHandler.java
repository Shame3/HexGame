package com.hex.controller;

import com.hex.serviceimpl.LoginServiceImpl;
import com.hex.serviceimpl.UserServiceImpl;
import com.hex.util.ApiResultHandler;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import com.hex.entity.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
public class LoginHandler {
    //未解决多人同时登录问题
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginServiceImpl loginService;
    @RequestMapping ("/login")//http://localhost:8080/login?name=1&password=1
    public ApiResult login(@RequestParam("name") String name,@RequestParam("password") String password) {
        User user=loginService.login(name,password);
        if (user!= null) {
            request.getSession().setAttribute("userId",user.id);
            return ApiResultHandler.buildApiResult(200, "登录成功", user);
        }
        return ApiResultHandler.buildApiResult(400, "登录失败", null);
    }

    @RequestMapping ("/loginAdmin")//http://localhost:8080/loginAdmin?name=123&password=123
    public ApiResult loginAdmin(@RequestParam("name") String name,@RequestParam("password") String password) {
        Admin admin=loginService.loginAdmin(name,password);
        if (admin!= null) {
            request.getSession().setAttribute("adminId",admin.id);
            return ApiResultHandler.buildApiResult(200, "登录成功", admin);
        }
        return ApiResultHandler.buildApiResult(400, "登录失败", null);
    }
    @RequestMapping(value = "/login/error")
    @ResponseBody
    public ApiResult error(){
        return ApiResultHandler.buildApiResult(500, "重新登录", null);
    }

    @RequestMapping(value = "/login/out")
    @ResponseBody
    public ApiResult out(){
        request.getSession().invalidate();
        return ApiResultHandler.buildApiResult(400, "退出成功", null);
    }
}
