package com.hex.service;

import com.hex.entity.Admin;
import com.hex.entity.User;

import java.util.List;

public interface AdminService {
    public List<Admin> findAll();

    public Admin findById(Integer adminId);

    public List<Admin> findByName(String adminName);

    public int deleteById(int adminId);

    public int update(Admin admin);

    public int add(Admin admin);
}
