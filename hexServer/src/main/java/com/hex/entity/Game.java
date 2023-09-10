package com.hex.entity;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Game {
    public int id;
    public int tableId;
    public int next;//0代表红，1代表蓝
    public int winner;
}
