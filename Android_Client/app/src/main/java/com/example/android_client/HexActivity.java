package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.ToastUtils;

public class HexActivity extends AppCompatActivity {
    static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        type=sp.getInt("id", 0);

        com.example.android_client.bean.ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(HexActivity.this);
    }
    public void enterAi (View view){
        startActivity(new Intent(getApplicationContext(),AiActivity.class));
    }
    public void enterSingle (View view){
        startActivity(new Intent(getApplicationContext(),SingleGameActivity.class));
    }
    public void enterHall (View view){
        if (type==0) {
            ToastUtils toastUtils = new ToastUtils();
            toastUtils.show(getApplicationContext(), "游客不可联网对战");
        }
        else startActivity(new Intent(getApplicationContext(),HallActivity.class));
    }
    public void enterDetail (View view){
        if (type==0) {
            ToastUtils toastUtils = new ToastUtils();
            toastUtils.show(getApplicationContext(), "游客不可进入");
        }
        else startActivity(new Intent(getApplicationContext(),DetailActivity.class));
    }
    public void enterMain (View view){
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
        ActivityManager exitM = ActivityManager.getInstance();
        exitM.exit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onBackPressed() {
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();

        super.onBackPressed();
        ActivityManager exitM = ActivityManager.getInstance();
        exitM.exit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}