package com.hex.service;

import com.hex.entity.Step;
import com.hex.entity.User;

import java.util.List;

public interface StepService {
    public List<Step> findAll();

    public List<Step> findByGameId(Integer gameId);

    public int deleteById(int id);

    public int deleteByGameId(int gameId);

    public int update(Step step);

    public int add(Step step);
}
