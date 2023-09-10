package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.ChessTable;
import com.example.android_client.bean.ToastUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class HallActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getName();
    private Runnable updateRunnable;
    private transient boolean stopFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(HallActivity.this);

        updateRunnable=new Runnable() {
            @Override
            public void run() {
                while (!stopFlag) {
                    setuphall();
                    try {
                        TimeUnit.SECONDS.sleep(3);
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
        this.stopFlag=false;
        new Thread(updateRunnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stopFlag=true;
    }
    public void returnHex(View view){
        stopFlag=true;
        startActivity(new Intent(getApplicationContext(),HexActivity.class));
    }

    private void setNameById(int id,TextView textView){
        String name;
        if (id<=0) return ;
        String url= getResources().getText(R.string.url)+"user/findById/"+id;
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                Gson gson=new Gson();
                Map<String ,Object> result=gson.fromJson(content, Map.class);
                Map<String,Object> user=(Map<String, Object>) result.get("data");
                textView.setText((String)user.get("name")+" ");
            }
        });
    }

    private void seat(String color,ChessTable chesstable){
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
        int id=sp.getInt("id",0);
        String url= getResources().getText(R.string.url)+"chessTable/joinTable?tableId="+chesstable.getId()
                +"&redOrblue="+color+"&userId="+id;
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                stopFlag=true;
                String content=response.body().string();
                Intent intent=new Intent(getApplicationContext(),GameActivity.class);
                //Intent intent=new Intent(getApplicationContext(),testActivity_1.class);
                intent.putExtra("tableId",chesstable.getId());
                startActivity(intent);
            }
        });
    }

    private void setuphall(){
        String url= getResources().getText(R.string.url)+"chessTable/findAll";
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
              //  Log.i(TAG,content);
                Gson gson=new Gson();
                ChessTable[] chess_tables=gson.fromJson(content, ChessTable[].class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
                        LinearLayout linearLayout=findViewById(R.id.hall); linearLayout.removeAllViews();
                        LinearLayout oneLine=null;
                        for (int i=0;i<chess_tables.length;i++){
                            ChessTable chesstable=chess_tables[i];
                            if (i%2==0){
                                oneLine=new LinearLayout(getApplicationContext());
                                oneLine.setOrientation(LinearLayout.HORIZONTAL);
                                linearLayout.addView(oneLine);
                            }
                            View view=getLayoutInflater().inflate(R.layout.table,null);
                            Button black_button=((Button) view.findViewById(R.id.black));
                            Button white_button=((Button) view.findViewById(R.id.white));

                            if (chesstable.getUserRed()>=0) {   //存在用户
                                black_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.drawable.people));
                                TextView blackView=((TextView )view.findViewById(R.id.blackName));
                                setNameById(chesstable.getUserRed(),blackView);
                                if (chesstable.getUserRed()==sp.getInt("id",0)){ //已经入座
                                    black_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.drawable.bat));
                                    black_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            stopFlag=true;
                                            Intent intent=new Intent(getApplicationContext(),GameActivity.class);
                                          //  Intent intent=new Intent(getApplicationContext(),testActivity_1.class);
                                            intent.putExtra("tableId",chesstable.getId());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                            else {   //不存在用户
                                black_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.down));
                                black_button.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        seat("red",chesstable);
                                    }
                                });
                            }

                            if (chesstable.getUserBlue()>=0) {
                                TextView whiteView=((TextView )view.findViewById(R.id.whiteName));
                                setNameById(chesstable.getUserBlue(),whiteView);
                                white_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.drawable.people));
                                if (chesstable.getUserBlue()==sp.getInt("id",0)){
                                    white_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.drawable.bat));
                                    white_button.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            stopFlag=true;
                                            Intent intent=new Intent(getApplicationContext(),GameActivity.class);
                                          //  Intent intent=new Intent(getApplicationContext(),testActivity_1.class);
                                            intent.putExtra("tableId",chesstable.getId());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                            else {
                                white_button.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.down));
                                white_button.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                       seat("blue",chesstable);
                                    }
                                });
                            }
                            oneLine.addView(view);
                        }
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        stopFlag=true;
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),HexActivity.class));
    }
}