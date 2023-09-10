package com.hex.mapper;

import com.hex.entity.Step;
import com.hex.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StepMapper {
    @Select("select * from step")
    public List<Step> findAll();

    @Select("select * from step where gameId = #{gameId}")
    public List<Step> findByGameId(Integer gameId);

    @Delete("delete from step where id = #{id}")
    public int deleteById(int id);

    @Delete("delete from step where gameId = #{gameId}")
    public int deleteByGameId(int gameId);

    @Update("update step set gameId = #{gameId},index = #{index},color = #{color} where id = #{id}")
    public int update(Step step);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into step (gameId,indexNum,color) values (#{gameId},#{indexNum},#{color})")
    public int add(Step step);
}
