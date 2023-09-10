package com.example.android_client.bean;

import lombok.Data;

public class ChessTable {
    public int id;
    public int userRed;   //-1代表为空
    public int userBlue;   //-1代表为空
    public int gameId;      //-1代表为空
    public java.sql.Timestamp up;

    public ChessTable(int id_,int userRed_,int userBlue_,int gameId_){
        id=id_;userRed=userRed_;userBlue=userBlue_;gameId=gameId_;
    }
    public int getId(){return id;}
    public int getUserRed(){return userRed;}
    public int getUserBlue(){return userBlue;}
    public int getGameId(){return getGameId();}

    public void setId(int id){this.id=id;}
    public void setUserRed(int user_red){this.userRed=user_red;}
    public void setUserBlue(int user_blue){this.userBlue=user_blue;}
    public void setGameId(int game_id){this.gameId=game_id;}
}


