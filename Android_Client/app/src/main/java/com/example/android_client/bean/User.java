package com.example.android_client.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public long id;
    public String name;
    public String password;
    public java.sql.Timestamp up;
    public User(){}
    public User(String name,String password){this.name=name;this.password=password;}
    public User(int id,String name,String password){this.id=id;this.name=name;this.password=password;}
}
