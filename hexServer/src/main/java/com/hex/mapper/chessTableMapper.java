package com.hex.mapper;

import com.hex.entity.User;
import com.hex.entity.chessTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface chessTableMapper {
    @Select("select id,userRed,userBlue,gameId,up from chessTable")
    public List<chessTable> findAll();

    @Select("select id,userRed,userBlue,gameId,up from chessTable where id= #{id}")
    public chessTable findById(Integer id);

    @Select("select id,userRed,userBlue,gameId,up from chessTable where userRed= #{id} or userBlue= #{id}")
    public chessTable findByUserId(Integer id);

    @Update("update chessTable set userRed = #{userRed},userBlue = #{userBlue},gameId = #{gameId} where id = #{id}")
    public int update(chessTable chessTable);
}
