package com.hex.service;

import java.util.List;
import com.hex.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

public interface UserService {
    public List<User> findAll();

    public User findById(Integer userId);

    public List<User> findByName(String username);

    public int deleteById(int userId);

    public int update(User user);

    public int add(User user);
}
