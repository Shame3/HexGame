package com.hex.service;

import com.hex.entity.User;
import com.hex.entity.chessTable;

import java.util.List;

public interface chessTableService {
    public List<chessTable> findAll();

    public chessTable findById(Integer id);

    public chessTable findByUserId(Integer id);

    public int update(chessTable chessTable);
}
