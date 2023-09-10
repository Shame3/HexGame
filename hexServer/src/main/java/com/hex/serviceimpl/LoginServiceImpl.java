package com.hex.serviceimpl;

import com.hex.entity.Admin;
import com.hex.entity.User;
import com.hex.mapper.LoginMapper;
import com.hex.mapper.UserMapper;
import com.hex.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public User login(String name, String password) {
        return loginMapper.login(name,password);
    }
    @Override
    public Admin loginAdmin(String adminName, String password) {
        return loginMapper.loginAdmin(adminName,password);
    }
    public List<User> loginName(String name){
        return loginMapper.loginName(name);
    }
}
