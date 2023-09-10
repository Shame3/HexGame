package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.android_client.bean.AI;
import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.Judge;
import com.example.android_client.bean.ToastUtils;

public class AiActivity extends AppCompatActivity {

    private final static String TAG=MainActivity.class.getName();
    private int tableId=1,gameId=-1,userId=1;
    public AI ai;
    private int myColor=-1;private  String name;
    static Judge judge=new Judge();  //棋盘胜负判断

    private Runnable updateRunnable;
    private transient boolean stopFlag=false;

    public class Panel extends View {    //棋盘
        float leg=27;   //设置底边长
        float arm=(float) Math.sqrt(3)*leg;float hyp=leg*2;float r=(float)( arm*0.7);
        public Panel (Context context){
            super(context);
        }
        @SuppressLint("ResourceAsColor")
        public void init(Canvas canvas) {
            canvas.drawColor(android.R.color.black);
            Paint paint=new Paint();
            Paint r_Paint=new Paint();r_Paint.setColor(Color.RED);r_Paint.setStrokeWidth(6); //红色棋子
            Paint b_Paint=new Paint();b_Paint.setColor(Color.BLUE);b_Paint.setStrokeWidth(6);  //蓝色棋子
            Paint mPaint = new Paint();mPaint.setAntiAlias(true); mPaint.setStrokeWidth(4);
            mPaint.setColor(Color.DKGRAY); mPaint.setTextSize(hyp);//提示信息
            paint.setColor(Color.BLACK);
            float x=0,y=leg;    //设置棋盘每一行起点;
            float x_0=0,y_0=leg;    //棋盘第一行起点

            for (int i=1;i<=11;i++){          //绘制棋盘轮廓
                x=x_0+arm*(i-1);y=y_0+(hyp+leg)*(i-1);   //第i行起点
                for (int j=1;j<=11;j++){
                    canvas.drawLine(x, y,x+arm, y-leg, paint);
                    if (i==1)  canvas.drawLine(x, y,x+arm, y-leg, r_Paint);
                    canvas.drawLine (x+arm, y-leg, x+arm*2,y, paint);
                    if (i==1) canvas.drawLine ( x+arm, y-leg,x+arm*2, y, r_Paint);
                    if (j==11) canvas.drawLine ( x+arm, y-leg, x+arm*2,y, b_Paint);
                    canvas.drawLine( x+arm*2,y, x+arm*2,y+hyp,paint);
                    if (j==11)canvas.drawLine( x+arm*2,y, x+arm*2, y+hyp, b_Paint);
                    canvas.drawLine( x+arm*2, (y+hyp),x+arm,  y+hyp+leg, paint);
                    if (i==11)canvas.drawLine( x+arm*2, y+hyp,x+arm, y+hyp+leg, r_Paint);
                    canvas.drawLine(x+arm,  y+hyp+leg, x,  y+hyp, paint);
                    if (j==1) canvas.drawLine(x+arm,  y+hyp+leg, x,  y+hyp, b_Paint);
                    if (i==11)  canvas.drawLine(x+arm, y+hyp+leg, x,  y+hyp, r_Paint);
                    canvas.drawLine(x,  y+hyp,x,  y, paint);
                    if (j==1)canvas.drawLine(x, y+hyp, x,  y, b_Paint);
                    if (judge.used[i*11+j]!=0){
                        Paint paint_now=new Paint();
                        if (judge.used[i*11+j]==1)  paint_now.setColor(Color.RED);
                        else paint_now.setColor(Color.BLUE);
                        canvas.drawCircle(x+arm, y+leg, r, paint_now);
                    }
                    x+=arm*2;
                }
            }

            if (myColor==-1){
                canvas.drawText("选择红色方", arm*7,arm*22, mPaint);
                canvas.drawText("选择蓝色方", arm*17,arm*22, mPaint);
            }else {
                canvas.drawText("", arm*7,arm*22, mPaint);
                canvas.drawText("", arm*17,arm*22, mPaint);
            }
            canvas.drawText("退出对局", arm*32,arm*6, mPaint);  //离开
            canvas.drawText("悔棋", arm*32,arm*9, mPaint);  //悔棋
            if (judge.judgesf()!=0){
                if (judge.judgesf()==1) {
                    judge.winner= 1; r_Paint.setTextSize(60);
                    canvas.drawText("红方胜利", arm * 32, arm * 12, r_Paint);
                }
                else {
                    judge.winner=2;b_Paint.setTextSize(60);
                    canvas.drawText("蓝方胜利", arm*32,arm*12, b_Paint);
                }
            }
            else {
                if (gameId==-1) canvas.drawText("游戏未开始", arm*32,arm*12, mPaint);
                else {
                    if (judge.stepTurn == myColor)
                        canvas.drawText("请您落子", arm * 32, arm * 12, mPaint);
                    else  canvas.drawText("等待AI落子", arm * 32, arm * 12, mPaint);
                }
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            init(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {  //棋盘点击事件
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP){
                float px=event.getX();float py=event.getY();
                ToastUtils toastUtils=new ToastUtils();
            //    toastUtils.show(getApplicationContext(),px/arm+" "+py/arm);
                if (py>=21*arm&&myColor==-1){     //选择颜色
                    if (px>=7*arm&&px<=12.5*arm) {myColor=0;gameId=1;invalidate();return true;}
                    else if (px>=17*arm&&px<=22.5*arm) {myColor=1;gameId=1;stopFlag=false; onResume();return true;}
                }
                if ((px>=arm*32)&&(py>=arm*4.6&&py<=arm*5.6)){   //离开
                    for (int i=1;i<=150;i++) {
                        judge.used[i]=0; judge.winner=0; judge.tot=0;
                    }
                    Intent intent=new Intent(getApplicationContext(),HexActivity.class);
                    startActivity(intent);
                }
                else if ((px>=arm*32)&&(py>=arm*7.6&&py<=arm*8.6)){   //悔棋
                    if (judge.tot<=0) toastUtils.show(getApplicationContext(),"棋盘为空！");
                    else if (judge.winner!=0) toastUtils.show(getApplicationContext(),"已出现获胜者！");
                    else {
                        judge.used[judge.st[judge.tot]]=0;judge.tot--;judge.stepTurn=1-judge.stepTurn;
                        invalidate();
                        if (judge.stepTurn!=myColor)  stopFlag=false; onResume();
                    }
                }
                if (judge.winner!=0)  return true;

                float x=0,y=leg; float x_0=0,y_0=leg;
                for (int i=1;i<=11;i++){
                    x=x_0+arm*(i-1);y=y_0+(float)(leg+hyp)*(i-1);
                    for (int j=1;j<=11;j++){
                        if((px>=x&&px<=x+arm*2)&&(py>=y-leg&&py<=y+hyp)){
                            int now=i*11+j;
                            if (judge.used[i*11+j]==1||judge.used[i*11+j]==2){
                                Toast.makeText(getApplicationContext(), "该处已有落子", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            else {
                                if (gameId==-1) {
                                    Toast.makeText(getApplicationContext(), "游戏未开始,请选择所执棋子", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                else {
                                    if (judge.stepTurn == myColor) {
                                        judge.st[++judge.tot] = now;  //悔棋。。。
                                        judge.used[now] = judge.stepTurn + 1;judge.stepTurn=1-myColor;
                                        invalidate();
                                        stopFlag=false; onResume();
                                    } else Toast.makeText(getApplicationContext(), "当前不是你走子", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        }
                        x+=arm*2;
                    }
                }
            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        judge.stepTurn=0;
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new AiActivity.Panel(getApplicationContext()));

        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(AiActivity.this);

        stopFlag=true;
        updateRunnable=new Runnable() {
            @Override
            public void run() {
                long startTime =  System.currentTimeMillis();
                while (judge.stepTurn!=myColor&&myColor!=-1&&judge.winner==0) {
                    Judge to=new Judge(judge); ai=new AI(to,judge.stepTurn);
                    int choose=ai.winNext();
                    if(choose!=-1) {
                        System.out.println("AI选择：" + choose);
                        if (judge.stepTurn!=myColor) {
                            judge.used[choose] = judge.stepTurn + 1;
                            judge.stepTurn = 1 - judge.stepTurn;
                            judge.st[++judge.tot] = choose;
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    setContentView(new AiActivity.Panel(getApplicationContext()));
                                }
                            };
                            runOnUiThread(runnable);
                        }
                    }
                    else stopFlag=true;
                    long endTime =  System.currentTimeMillis();
                    long usedTime = (endTime-startTime)/1000;
                    System.out.println("AI耗时:"+usedTime);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.stopFlag=false;
        new Thread(updateRunnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stopFlag=true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        for (int i=1;i<=150;i++) {
            judge.used[i]=0;judge.winner=0;judge.tot=0;
        }
        Intent intent=new Intent(getApplicationContext(),HexActivity.class);
        startActivity(intent);
    }
}