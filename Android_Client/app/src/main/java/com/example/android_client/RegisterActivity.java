package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.ToastUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getName();
    public static OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getApplication().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT){
            ImageView imageView=(ImageView) findViewById(R.id.imageView4);
            imageView.getBackground().setAlpha(0);
            imageView.setAlpha(0);
        }
        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(RegisterActivity.this);
    }
    public void doRegister(View view){
        String username=((EditText)findViewById(R.id.registerName)).getText().toString();
        String password=((EditText)findViewById(R.id.registerPassword)).getText().toString();
        String password1=((EditText)findViewById(R.id.registerPassword2)).getText().toString();
        if (username==null||username.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (password==null||password.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (!password.equals(password1)){
            Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
            return ;
        }
        String url= MessageFormat.format(getResources().getText(R.string.url)+"//user/save?name={0}&password={1}",username,password);
        Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG,"failure");
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                Gson gson=new Gson();
                Map<String ,Object> result=gson.fromJson(content, Map.class);
                double code_=(double) result.get("code"); int code=new Double(code_).intValue();
                if (code==200){
                    Map<String,Object> user=(Map<String, Object>) result.get("data");
                    double id_=(double) user.get("id"); int id=new Double(id_).intValue();
                    String name=(String) user.get("name"); String password=(String) user.get("password");

                    SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
                    editor.putInt("id",id); editor.putString("name",name); editor.putString("password",password);
                    editor.commit();
                 //   ToastUtils toastUtils=new ToastUtils();
                    //toastUtils.show(getApplicationContext(),"注册成功");
                    startActivity(new Intent(getApplicationContext(),HexActivity.class));
                }
                else {
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"该用户名已存在");
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}