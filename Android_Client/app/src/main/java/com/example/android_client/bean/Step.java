package com.example.android_client.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Step {
    public int id;
    public int gameId;
    public int indexNum;
    public int color;//0 黑色，1 白色
}
