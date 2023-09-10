package com.hex.service;

import com.hex.entity.Admin;
import com.hex.entity.User;

import java.util.List;

public interface LoginService {
    public User login(String name, String password);
    public Admin loginAdmin(String adminName, String password);
    public List<User> loginName(String name);
}
