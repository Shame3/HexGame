package com.hex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class chessTable {
    public int id;
    public int userRed;   //-1代表为空
    public int userBlue;   //-1代表为空
    public int gameId;      //-1代表为空
    public Timestamp up;
    public chessTable(int id_,int userBlack_,int userWhite_,int gameId_){
        id=id_;userRed=userBlack_;userBlue=userWhite_;gameId=gameId_;
    }
}


