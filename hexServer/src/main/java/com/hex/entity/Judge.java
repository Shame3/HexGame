package com.hex.entity;

import java.io.FileInputStream;
import java.util.Vector;

//棋盘判胜
public class Judge {
    public Vector<Integer> step;
    public Judge(){ }
    public int used[] = new int[200];      //棋盘状态
    int ff_1[]=new int[200],ff_2[]=new int [200];                  //并查集判胜
    int flag=1;
    int winner=0;                //执棋者，胜利者
    int S_1=132,T_1=133,S_2=134,T_2=135;     //超级源点，超级汇点
    int dir[][]={{-1,0},{1,0},{0,1},{0,-1},{-1,1},{1,-1}};  //方向数组
    int st[]=new int [1000];             //棋盘落子记录
    int tot=0;

    int Find_1(int x) {
        if(x==ff_1[x])  return x;    return ff_1[x]=Find_1(ff_1[x]);
    }
    int Find_2(int y) {
        if(y==ff_2[y])  return y;    return ff_2[y]=Find_2(ff_2[y]);
    }
    void merge_1(int x,int y) {
        int xx=Find_1(x);   int yy=Find_1(y);  ff_1[xx]=yy;
    }
    void merge_2(int x,int y) {
        int xx=Find_2(x);   int yy=Find_2(y);  ff_2[xx]=yy;
    }
    int ok(int x,int y,int xx,int yy)  //判断落子合法
    {
        if(xx<1 || yy<1 || xx>11 || yy>11)  return 0;
        if(used[x*11+y]!=used[xx*11+yy])  return 0;
        return 1;
    }
    public int judgesf()
    {
        for(int i=0;i<=150;i++)  {ff_1[i]=i;ff_2[i]=i;}
        for(int i=1;i<=11;i++)
            for(int j=1;j<=11;j++)
            {
                if(used[i*11+j] !=0)
                {
                    int ind=11*i+j;
//                    if(used[i*11+j]==1)
//                    {
//                        if(i==1)  merge_1(ind,S_1);
//                        if(i==11) merge_1(ind,T_1);
//                    }
//                    if(used[i*11+j]==2)
//                    {
//                        if(j==1) merge_2(ind,S_2);
//                        if(j==11) merge_2(ind,T_2);
//                    }
                    for(int k=0;k<6;k++)
                    {
                        int xx=i+dir[k][0],yy=j+dir[k][1];
                        if(ok(i,j,xx,yy)==0) continue;
                        if (used[i*11+j]==1)  merge_1(ind,11*xx+yy);
                        else if (used[i*11+j]==2) merge_2(ind,11*xx+yy);
                    }
                }
            }
//        if(Find_1(S_1)==Find_1(T_1))  return 1;
//        if(Find_2(S_2)==Find_2(T_2))  return 2;
        for (int i=12;i<=22;i++)
            for (int j=122;j<=132;j++)
                if (Find_1(i)== Find_1(j))
                    return 1;
        for (int i=1;i<=11;i++)
            for (int j=1;j<=11;j++)
                if (Find_2(i*11+1)== Find_2(j*11+11))
                    return 2;
        return 0;
    }

}
