package com.hex.mapper;

import com.hex.entity.Admin;
import com.hex.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface AdminMapper {
    @Select("select id,name,password from admin")
    public List<Admin> findAll();

    @Select("select id,name,password from admin where id = #{adminId}")
    public Admin findById(Integer adminId);

    @Select("select id,name,password from admin where name like concat('%',#{adminName},'%')")
    public List<Admin> findByName(String adminName);

    @Delete("delete from admin where id = #{adminId}")
    public int deleteById(int adminId);

    @Update("update admin set name = #{name},password = #{password} where id = #{id}")
    public int update(Admin admin);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into admin (name,password) values (#{name},#{password})")
    public int add(Admin admin);
}
