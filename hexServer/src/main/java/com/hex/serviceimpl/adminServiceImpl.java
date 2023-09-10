package com.hex.serviceimpl;

import com.hex.entity.Admin;
import com.hex.mapper.AdminMapper;
import com.hex.mapper.UserMapper;
import com.hex.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class adminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public Admin findById(Integer adminId) {
        return adminMapper.findById(adminId);
    }

    @Override
    public List<Admin> findByName(String adminName) {
        return adminMapper.findByName(adminName);
    }

    @Override
    public int deleteById(int adminId) {
        return adminMapper.deleteById(adminId);
    }

    @Override
    public int update(Admin admin) {
        return adminMapper.update(admin);
    }

    @Override
    public int add(Admin admin) {
        return adminMapper.add(admin);
    }
}
