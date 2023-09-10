package com.example.android_client.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Game {
    public int id;
    public int tableId;
    public int next;//0代表红，1代表蓝
    public int winner;//-1代表没有胜者
    public java.sql.Timestamp up;
    public Game(int id_,int tableId_,int next_,int winner_){
        id=id_;tableId=tableId_;next=next_;winner=winner_;
    }
}