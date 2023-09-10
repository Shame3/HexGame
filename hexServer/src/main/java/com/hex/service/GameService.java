package com.hex.service;

import com.hex.entity.Game;
import com.hex.entity.Step;

import java.util.List;

public interface GameService {
    public List<Game> findAll();

    public Game findById(Integer id);

    public Game findBytableId(Integer tableId);

    public int deleteById(int id);

    public int deleteBytableId(int tableId);

    public int update(Game game);

    public int add(Game game);
}
