package com.example.android_client.bean;

import android.content.Intent;

import java.util.LinkedList;
import java.util.Queue;

public class Dinic {
    public Dinic(Judge judge,int color){
        q=new LinkedList<Integer>() ;
        for (int i=0;i<150;i++) used[i]=judge.used[i];
        this.color=color;
    }
    public int []used=new int [150];
    public int edge[]=new int[20000],
            to[]=new int [20000],
            head[]=new int [20000],
            Next[]=new int[20000],
            d[]=new int[1000],
            f[]=new int[300];
    public int s,t,color,inf=100000000,tot=0;
    public Queue<Integer> q;

    public int [][]dir={{-1,0},{1,0},{0,1},{0,-1},{-1,1},{1,-1}};
    public int ind_x(int index) {return (index-1)/11;}
    public int ind_y(int index){return (index-1)%11+1;}
    public int ind_index(int x,int y){return x*11+y;}
    void add(int x,int y,int z){
        edge[++tot]=z; to[tot]=y; Next[tot]=head[x]; head[x]=tot;
        edge[++tot]=0; to[tot]=x; Next[tot]=head[y]; head[y]=tot;
    }

    boolean bfs(){
        for (int i=0;i<1000;i++) d[i]=0;
        if (!q.isEmpty())
        q.clear();
        q.add(s); d[s]=1;
        while(!q.isEmpty()){
            int x=q.peek();q.remove();
            for(int i=head[x];i!=0;i=Next[i]){
                if((edge[i]!=0)&&(d[to[i]]==0)){
                    q.add(to[i]); d[to[i]]=d[x]+1;
                    if(to[i]==t){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    int dinic_(int x,int flow){
        if(x==t)return flow;
        int rest=flow,k;
        for(int i=head[x];(i!=0) && (rest!=0) ;i=Next[i]){
            if((edge[i]!=0) && (d[to[i]]==d[x]+1)){
                k=dinic_(to[i],Math.min(rest,edge[i]));
                if(k==0) d[to[i]]=0;
                edge[i]-=k; edge[i^1]+=k; rest-=k;
            }
        }
        return flow -rest;
    }
    int find_maxflow(){
        build();
        int maxflow=0; int flow=0;
        while(bfs()){
            while((flow=dinic_(s,100000))!=0) maxflow+=flow;
        }
        return maxflow;
    }

    void build(){
        if (color==0){
            s=0; t=140;
            for (int i=12;i<=22;i++)  if (used[i]!=2-color )add(s,i,inf);
            for (int i=122;i<=132;i++)  if (used[i]!=2-color ) add(i,t,inf);
        }
        else {
            s=0; t=140;
            for (int i=1;i<=11;i++) if (used[i*11+1]!=2-color ) add(s,i*11+1,inf);
            for (int i=1;i<=11;i++) if (used[i*11+11]!=2-color ) add(i*11+11,t,inf);
        }

        for (int index=12;index<=132;index++){
            int value=1;
            if (used[index]==2-color) continue;
            if (used[index]==color+1) value+=1;
                int ind_x=ind_x(index),ind_y=ind_y(index);
                for (int i=0;i<6;i++){
                    int to_x=ind_x+dir[i][0];int to_y=ind_y+dir[i][1];int to=ind_index(to_x,to_y);
                    if (to>=12&&to<=132&&used[to]!=2-color){
                        int now_value=value;if (used[to]==color+1) now_value+=1;
                        if (color==0){
                            if (to_x>ind_x) add(index,to,now_value);
                            else if (to_x==ind_x&&to_y>ind_y) add(index,to,now_value);
                        }
                        else {
                            if (to_y>ind_y) add(index,to,now_value);
                            else if (to_y==ind_y&&to_x>ind_x) add(index,to,now_value);
                        }
                    }
                }
            //}
        }
    }
}
