package com.hex.controller;

import com.hex.entity.ApiResult;
import com.hex.service.LoginService;
import com.hex.service.UserService;
import com.hex.serviceimpl.LoginServiceImpl;
import com.hex.serviceimpl.UserServiceImpl;
import com.hex.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hex.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserHandler {
    @Autowired private HttpServletRequest request;
    @Autowired private UserServiceImpl userService;
    @Autowired private LoginServiceImpl loginService;

    @GetMapping("/findAll")//http://localhost:8080/user/findAll/
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/findById/{id}")  //http://localhost:8080/user/findById/1
    public ApiResult findById(@PathVariable("id")int id){
        User user=userService.findById(id);
        if (user==null)  return ApiResultHandler.buildApiResult(400, "该用户不存在", null);
        else return ApiResultHandler.buildApiResult(200, "success",user);
    }
    @GetMapping("/findByName/{username}")//http://localhost:8080/user/findByName/3
    public Collection<User> findByName(@PathVariable("username")String username){
        return userService.findByName(username);
    }

    @RequestMapping("/save")//http://localhost:8080/user/save?name=conan&password=3
    public ApiResult add(@RequestParam("name") String name, @RequestParam("password") String password) {
        Collection<User> users=loginService.loginName(name);
        if (users.size()>=1) return ApiResultHandler.buildApiResult(400,"该用户名已存在",null);
        User user =new User(name,password); userService.add(user);  user=loginService.login(name,password);
        if (user!= null) {
            if( request.getSession().getAttribute("userId")==null&&request.getSession().getAttribute("adminId")==null)
            request.getSession().setAttribute("userId",user.id);
            return ApiResultHandler.buildApiResult(200, "添加成功", user);
        }
        return ApiResultHandler.buildApiResult(400, "添加失败", null);
    }

    @RequestMapping("/update")//http://localhost:8080/user/update?id=3&name=c&password=1
    public ApiResult update(@RequestParam("id") int id,@RequestParam("name") String name,
                       @RequestParam("password") String password){
        List<User> users=loginService.loginName(name);
        if (users.size()>1) return ApiResultHandler.buildApiResult(400, "该用户名已存在", null);
        else if (users.size()==1){
            if (users.get(0).id!=id ) {
                return ApiResultHandler.buildApiResult(400, "该用户名已存在", null);
            }
        }
        userService.update(new User(id,name,password));
        User user=userService.findById(id);
        if (user.name.equals(name)&&user.password.equals(password))
            return ApiResultHandler.buildApiResult(200, "更新成功", user);
        else return ApiResultHandler.buildApiResult(400, "更新失败", null);
    }

    @GetMapping("/deleteById/{id}")//http://localhost:8080/user/deleteById/14
    public ApiResult deleteById(@PathVariable("id")int id){
        userService.deleteById(id);
        User user=userService.findById(id);
        if (user==null) return ApiResultHandler.buildApiResult(200, "删除成功",null);
        else return ApiResultHandler.buildApiResult(400, "删除失败", user);
    }

}
