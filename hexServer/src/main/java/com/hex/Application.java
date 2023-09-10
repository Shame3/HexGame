package com.hex;

import com.hex.entity.chessTable;
import com.hex.serviceimpl.GameServiceImpl;
import com.hex.serviceimpl.StepServiceImpl;
import com.hex.serviceimpl.chessTableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
