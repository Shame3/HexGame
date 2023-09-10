package com.hex.mapper;

import com.hex.entity.Game;
import com.hex.entity.chessTable;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GameMapper {
    @Select("select id,tableId,next,winner from game")
    public List<Game> findAll();

    @Select("select id,tableId,next,winner from game where id= #{id}")
    public Game findById(Integer id);

    @Select("select id,tableId,next,winner from game where tableId= #{tableId}")
    public Game findBytableId(Integer tableId);

    @Update("update game set tableId=#{tableId},next = #{next},winner = #{winner} where id = #{id}")
    public int update(Game game);

    @Delete("delete from game where id = #{id}")
    public int deleteById(int id);

    @Delete("delete from game where tableId = #{tableId}")
    public int deleteBytableId(int tableId);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into game(tableId,next,winner) values(#{tableId},#{next},#{winner})")
    public int add(Game game);
}
