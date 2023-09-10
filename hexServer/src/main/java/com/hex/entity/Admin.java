package com.hex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Admin {
    public long id;
    public String name;
    public String password;
    public Admin(String name,String password){this.name=name;this.password=password;}
    public Admin(int id,String name,String password){this.id=id;this.name=name;this.password=password;}
}
