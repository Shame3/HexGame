package com.example.android_client.bean;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class AI {
    public int color;
    public Judge judge;
    public int count[]=new int[150];
    public int have_add[]=new int[150];
    public Vector<Integer> prepare;
    int []canChoose=new int [150];
    public int best=72;
    public double bestRate=-1.0;

    public int [][]dir={{-1,0},{1,0},{0,1},{0,-1},{-1,1},{1,-1}};
    public int ind_x(int index) {return (index-1)/11;}
    public int ind_y(int index){return (index-1)%11+1;}
    public int ind_index(int x,int y){return x*11+y;}
    public AI(Judge judge_ ,int color_){
        judge=new Judge(judge_);
        color=color_;
    }

    public void connect(){
        for (int index=12;index<=132;index++) {
            int now_x=ind_x(index),now_y=ind_y(index);
            if (judge.used[index]!=0) {
                for (int i = 0; i < 6; i++) {
                    int to1_x = now_x + dir[i][0], to1_y = now_y + dir[i][1];
                    for (int j = 0; j < 6; j++) {
                        int to2_x = to1_x + dir[j][0], to2_y = to1_y + dir[j][1];
                        int to2 = ind_index(to2_x, to2_y);
                        if (to2 >= 12 && to2 <= 132 && judge.used[to2] == 0&&have_add[to2]==0) {
                            prepare.add(to2);have_add[to2]=1;
                        }
                    }
                }
            }
        }
    }
    public int random(int num){
        Random r = new Random();
        int now=r.nextInt(num);
        return now;
    }
    public int winNext(){
        //查开局库
        String s="";
        for (int i=12;i<=132;i++) s+=judge.used[i];
        Data data=new Data(s);
        if (data.findIn()!=-1) return data.findIn();
        prepare=new Vector<Integer>();
        for (int i=12;i<=132;i++) { have_add[i]=0;  }
        connect(); Collections.sort(prepare);
        int prepare_num=prepare.size();
        for (int i=0;i<prepare_num;i++){
            int now_index=prepare.get(i);
            count[now_index]=150;
            int win_num=0;
            Judge x=new Judge(judge);x.used[now_index]=color+1;
            for (int j=0;j<count[now_index];j++) {
                for (int index=12;index<=132;index++) x.used[index]=judge.used[index];
                x.used[now_index]=color+1;x.winner=-1;
                win_num += test(x);
            }
            double rate_now=win_num*1.0/count[now_index];
            System.out.println("落子"+now_index+"：胜率"+" " +win_num+" "+count[now_index]+" " +rate_now);
            if (rate_now>bestRate){
                best=now_index;bestRate=rate_now;
            }
        }
        if (bestRate<=0)return -1;
        return best;
    }
    public int test(Judge now){
        int nowColor=1-color,nowWin=0,tot=0;
        for (int i=12;i<=132;i++)
            if (now.used[i]==0)  canChoose[tot++]=i;
        while (true){
           if (tot<=1) break;
           int random=random(tot);
           now.used[canChoose[random]]=nowColor+1;canChoose[random]=canChoose[--tot];
           nowColor=1-nowColor;
       }
        nowWin=now.judgesf();
       if (nowWin==color+1) return 1;
       return 0;
    }

}
