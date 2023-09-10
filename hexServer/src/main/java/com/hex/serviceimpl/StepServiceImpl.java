package com.hex.serviceimpl;

import com.hex.entity.Step;
import com.hex.entity.User;
import com.hex.mapper.StepMapper;
import com.hex.mapper.UserMapper;
import com.hex.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepServiceImpl implements StepService {
    @Autowired
    private StepMapper stepMapper;
    @Override
    public List<Step> findAll() {
        return stepMapper.findAll();
    }

    @Override
    public List<Step> findByGameId(Integer gameId) {
        return stepMapper.findByGameId(gameId);
    }

    @Override
    public int deleteById(int id) {
        return stepMapper.deleteById(id);
    }

    @Override
    public int deleteByGameId(int gameId) {
        return stepMapper.deleteByGameId(gameId);
    }

    @Override
    public int update(Step step) {
        return stepMapper.update(step);
    }

    @Override
    public int add(Step step) {
        return stepMapper.add(step);
    }
}
