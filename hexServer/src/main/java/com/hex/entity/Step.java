package com.hex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step {
    public int id;
    public int gameId;
    public int indexNum;
    public int color;//0 红色，1 蓝色

}
