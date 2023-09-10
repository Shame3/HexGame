package com.hex.mapper;
import com.hex.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select id,name,password from user")
    public List<User> findAll();

    @Select("select id,name,password from user where id = #{userId}")
    public User findById(Integer userId);

    @Select("select id,name,password from user where name like concat('%',#{username},'%')")
    public List<User> findByName(String username);

    @Delete("delete from user where id = #{userId}")
    public int deleteById(int userId);

    @Update("update user set name = #{name},password = #{password} where id = #{id}")
    public int update(User user);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (name,password) values (#{name},#{password})")
    public int add(User user);
}
