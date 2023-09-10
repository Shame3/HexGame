package com.hex.serviceimpl;

import com.hex.entity.Game;
import com.hex.mapper.GameMapper;
import com.hex.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameMapper gameMapper;

    @Override
    public List<Game> findAll() {
        return gameMapper.findAll();
    }

    @Override
    public Game findById(Integer id) {
        return gameMapper.findById(id);
    }

    @Override
    public Game findBytableId(Integer tableId){  return gameMapper.findBytableId(tableId); }

    @Override
    public int deleteById(int id) { return gameMapper.deleteById(id); }

    @Override
    public int deleteBytableId(int tableId) {  return gameMapper.deleteBytableId(tableId); }

    @Override
    public int update(Game game) { return gameMapper.update(game); }

    @Override
    public int add(Game game) {
        return gameMapper.add(game);
    }
}
