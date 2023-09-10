package com.hex.serviceimpl;

import com.hex.entity.User;
import com.hex.mapper.UserMapper;
import com.hex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() { return userMapper.findAll(); }

    @Override
    public User findById(Integer userId) { return userMapper.findById(userId); }

    @Override
    public List<User> findByName(String username) { return userMapper.findByName(username); }

    @Override
    public int deleteById(int userId) { return userMapper.deleteById(userId); }

    @Override
    public int update(User user) { return userMapper.update(user); }

    @Override
    public int add(User user) { return userMapper.add(user); }
}
