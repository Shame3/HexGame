package com.hex.mapper;

import com.hex.entity.Admin;
import com.hex.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("select id,name,password from user where name= #{username} and password = #{password}")
    public User login(String username, String password);

    @Select("select id,name,password from admin where name= #{adminName} and password = #{password}")
    public Admin loginAdmin(String adminName, String password);

    @Select("select id,name,password from user where name= #{username} ")
    public List<User> loginName(String username);
}
