package com.hex.serviceimpl;

import com.hex.entity.chessTable;
import com.hex.mapper.UserMapper;
import com.hex.mapper.chessTableMapper;
import com.hex.service.chessTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class chessTableServiceImpl implements chessTableService {
    @Autowired
    private com.hex.mapper.chessTableMapper chessTableMapper;
    @Override
    public List<chessTable> findAll() {
        return chessTableMapper.findAll();
    }

    @Override
    public chessTable findById(Integer id) {
        return chessTableMapper.findById(id);
    }
    @Override
    public chessTable findByUserId(Integer id){
        return chessTableMapper.findByUserId(id);
    }
    @Override
    public int update(chessTable chessTable){
        return chessTableMapper.update(chessTable);
    }
}
