package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;
import com.example.android_client.bean.*;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    private transient boolean stopFlag = false;
    private Runnable updateRunnable;
    private final static String TAG = MainActivity.class.getName();

    private int tableId = -1, gameId = -1, userId = -1;
    private int myColor=-1;
    private String name;
    static Judge judge = new Judge();  //棋盘胜负判断

    public class Panel extends View {    //棋盘
        float leg = 28;   //设置底边长
        float arm = (float) Math.sqrt(3) * leg;
        float hyp = leg * 2;
        float r = (float) (arm * 0.7);

        public Panel(Context context) {
            super(context);
        }

        @SuppressLint("ResourceAsColor")
        public void init(Canvas canvas) {

            canvas.drawColor(android.R.color.darker_gray);
            Paint paint = new Paint();
            Paint r_Paint = new Paint();r_Paint.setColor(Color.RED);r_Paint.setStrokeWidth(6); //红色棋子
            Paint b_Paint = new Paint();b_Paint.setColor(Color.BLUE);b_Paint.setStrokeWidth(6);  //蓝色棋子
            Paint mPaint = new Paint();mPaint.setAntiAlias(true);mPaint.setStrokeWidth(4);mPaint.setColor(Color.DKGRAY);mPaint.setTextSize(hyp);//提示信息
            paint.setColor(Color.BLACK);
            float x = 0, y = leg;    //设置棋盘每一行起点;
            float x_0 = 0, y_0 = leg;    //棋盘第一行起点
            for (int i = 1; i <= 11; i++) {          //绘制棋盘轮廓
                x = x_0 + arm * (i - 1);y = y_0 + (hyp + leg) * (i - 1);   //第i行起点
                for (int j = 1; j <= 11; j++) {
                    canvas.drawLine(x, y, x + arm, y - leg, paint);
                    if (i == 1) canvas.drawLine(x, y, x + arm, y - leg, r_Paint);
                    canvas.drawLine(x + arm, y - leg, x + arm * 2, y, paint);
                    if (i == 1) canvas.drawLine(x + arm, y - leg, x + arm * 2, y, r_Paint);
                    if (j == 11) canvas.drawLine(x + arm, y - leg, x + arm * 2, y, b_Paint);
                    canvas.drawLine(x + arm * 2, y, x + arm * 2, y + hyp, paint);
                    if (j == 11) canvas.drawLine(x + arm * 2, y, x + arm * 2, y + hyp, b_Paint);
                    canvas.drawLine(x + arm * 2, (y + hyp), x + arm, y + hyp + leg, paint);
                    if (i == 11)
                        canvas.drawLine(x + arm * 2, y + hyp, x + arm, y + hyp + leg, r_Paint);
                    canvas.drawLine(x + arm, y + hyp + leg, x, y + hyp, paint);
                    if (j == 1) canvas.drawLine(x + arm, y + hyp + leg, x, y + hyp, b_Paint);
                    if (i == 11) canvas.drawLine(x + arm, y + hyp + leg, x, y + hyp, r_Paint);
                    canvas.drawLine(x, y + hyp, x, y, paint);
                    if (j == 1) canvas.drawLine(x, y + hyp, x, y, b_Paint);
                    if (judge.used[i * 11 + j] != 0) {
                        Paint paint_now = new Paint();
                        if (judge.used[i * 11 + j] == 1) paint_now.setColor(Color.RED);
                        else paint_now.setColor(Color.BLUE);
                        canvas.drawCircle(x + arm, y + leg, r, paint_now);
                    }
                    x += arm * 2;
                }
            }

            //提示信息
            canvas.drawText("退出对局", arm * 30, arm * 6, mPaint);  //离开
            if (judge.judgesf() != 0) {
                if (judge.judgesf() == 1) {
                    judge.winner = 1; r_Paint.setTextSize(60);
                    if (myColor==0) canvas.drawText("恭喜获胜", arm * 30, arm * 12, r_Paint);
                    else  canvas.drawText("对手获胜", arm * 30, arm * 12, r_Paint);
                } else {
                    judge.winner = 2;b_Paint.setTextSize(60);
                    if (myColor==1)  canvas.drawText("恭喜获胜", arm * 30, arm * 12, b_Paint);
                    else canvas.drawText("对手获胜", arm * 30, arm * 12, b_Paint);
                }
            } else {
                if (gameId == -1) canvas.drawText("游戏未开始", arm * 30, arm * 12, mPaint);
                else {
                    if (judge.stepTurn == myColor)
                        canvas.drawText("请您落子", arm * 30, arm * 12, mPaint);
                    else canvas.drawText("对方落子", arm * 30, arm * 12, mPaint);
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
            if (action == MotionEvent.ACTION_UP) {
                float px = event.getX();float py = event.getY();
//                ToastUtils toastUtils = new ToastUtils();
//                toastUtils.show(getApplicationContext(), "x:"+px/arm+"  y: "+py/arm);
                if ((px >= arm * 30) && (py >= arm * 4.6 && py <= arm * 5.6))  //离开
                    leaveTable();
                if (judge.winner != 0) return true;
                float x = 0, y = leg;
                float x_0 = 0, y_0 = leg;
                for (int i = 1; i <= 11; i++) {
                    x = x_0 + arm * (i - 1);
                    y = y_0 + (float) (leg + hyp) * (i - 1);
                    for (int j = 1; j <= 11; j++) {
                        if ((px >= x && px <= x + arm * 2) && (py >= y - leg && py <= y + hyp)) {
                            int now = i * 11 + j;
                            if (judge.used[i * 11 + j] == 1 || judge.used[i * 11 + j] == 2) {
                                Toast.makeText(getApplicationContext(), "该处已有落子", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                if(judge.winner!=0) return true;
                                else if (gameId == -1) {
                                    Toast.makeText(getApplicationContext(), "游戏未开始", Toast.LENGTH_SHORT).show();
                                    return true;
                                } else {
                                    if (judge.stepTurn == myColor) {
                                        //  judge.st[++judge.tot] = now;  //悔棋。。。
                                        judge.used[now] = judge.stepTurn + 1;
                                        luozi(i * 11 + j);
                                        invalidate();
                                    } else
                                        Toast.makeText(getApplicationContext(), "当前不是你走子", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        }
                        x += arm * 2;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new GameActivity.Panel(getApplicationContext()));

        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(GameActivity.this);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        userId = sp.getInt("id", 0);
        tableId = getIntent().getIntExtra("tableId", 0);
        judge.stepTurn = 0;

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                while (!stopFlag) {
                    String url = getResources().getText(R.string.url) + "chessTable/findById/" + tableId;
                    final Request request = new Request.Builder().url(url).build();
                    MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            ToastUtils toastUtils = new ToastUtils();
                            toastUtils.show(getApplicationContext(), "网络连接失败");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String content = response.body().string();
                            Gson gson = new Gson();
                            Map<String, Object> result = gson.fromJson(content, Map.class);
                            Map<String, Object> data = (Map<String, Object>) result.get("data");
                            if (data != null) {
                                double gameId_ = (double) data.get("gameId");gameId = new Double(gameId_).intValue();
                                double userBlack_ = (double) data.get("userRed");int userBlack = new Double(userBlack_).intValue();
                                double userWhite_ = (double) data.get("userBlue");int userWhite = new Double(userWhite_).intValue();
                                if (userId == userBlack) myColor = 0;
                                else if (userId == userWhite) myColor = 1;
                            }
                        }
                    });


                    url = getResources().getText(R.string.url) + "step/findByGameId/" + gameId;
                    final Request request_ = new Request.Builder().url(url).build();
                    MainActivity.okHttpClient.newCall(request_).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            ToastUtils toastUtils = new ToastUtils();
                            toastUtils.show(getApplicationContext(), "网络连接失败");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String content = response.body().string();
                            Gson gson = new Gson();
                            Log.i(TAG, "step" + content);
                            Step[] steps = gson.fromJson(content, Step[].class);
                            if (steps.length > 0) {
                                if (steps.length % 2 == 0) judge.stepTurn = 0;
                                else judge.stepTurn = 1;
                                for (int i = 0; i < steps.length; i++) {
                                    Step step = steps[i];
                                    if (step.color == 0) judge.used[step.indexNum] = 1;
                                    else judge.used[step.indexNum] = 2;
                                }
                            }
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    setContentView(new GameActivity.Panel(getApplicationContext()));
                                }
                            };
                            runOnUiThread(runnable);
                        }
                    });
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        stopFlag = false;
        new Thread(updateRunnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopFlag = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void luozi(int index) {
        String url = getResources().getText(R.string.url) + "step/" + userId + "/" + gameId + "/" + index;
        final Request request = new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils = new ToastUtils();
                toastUtils.show(getApplicationContext(), "网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Log.i(TAG,url);
            }
        });
    }

    public void leaveTable() {
        stopFlag=true;
        String url = getResources().getText(R.string.url) +"chessTable/leaveTable?tableId=" + tableId + "&userId=" + userId;
        for (int i = 1; i <= 140; i++) judge.used[i] = 0;
        final Request request = new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils = new ToastUtils();
                toastUtils.show(getApplicationContext(), "网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Intent intent = new Intent(getApplicationContext(), HallActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        leaveTable();
    }
}
