package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.ToastUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
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

public class MainActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getName();
    public static OkHttpClient okHttpClient;
    private  boolean flag=false;  //是否是游客
    private boolean admin=false;   //是否是管理员
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(MainActivity.this);

        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();

        ClearableCookieJar cookieJar=
                new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(getApplicationContext()));
        okHttpClient=new OkHttpClient.Builder().cookieJar(cookieJar).build();
    }
    public void doLogin(View view){
         String username=((EditText)findViewById(R.id.username)).getText().toString();
         String password=((EditText)findViewById(R.id.password)).getText().toString();
         if (username==null||username.trim().isEmpty()){
             Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
             return ;
         }
         if (password==null||password.trim().isEmpty()){
             Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
             return ;
         }

         else if (username.equals("hex")&&password.equals("hex")){
             flag=true;
             Intent intent=new Intent(getApplicationContext(),HexActivity.class);
             intent.putExtra("type",-1);
             startActivity(intent);
          }

         if(admin)
             url= MessageFormat.format(getResources().getText(R.string.url)+"loginAdmin?name={0}&password={1}",username,password);
         else url= MessageFormat.format(getResources().getText(R.string.url)+"login?name={0}&password={1}",username,password);
        // System.out.println(url);
         Request request=new Request.Builder().url(url).build();
         MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG,"error",e);
                System.out.println(e);
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                 String content=response.body().string();
                 Log.i(TAG,content);
                 Gson gson=new Gson();
                 Map<String ,Object> result=gson.fromJson(content, Map.class);
                 double code_=(double) result.get("code"); int code=new Double(code_).intValue();
                 if (code==200){
                     if (!admin) {
                         Map<String, Object> user = (Map<String, Object>) result.get("data");
                         double id_ = (double) user.get("id");
                         int id = new Double(id_).intValue();
                         String name = (String) user.get("name");
                         String password = (String) user.get("password");

                         SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                         SharedPreferences.Editor editor = sp.edit();
                         editor.putInt("id", id);
                         editor.putString("name", name);
                         editor.putString("password", password);
                         editor.commit();
                         startActivity(new Intent(getApplicationContext(),HexActivity.class));
                     }
                     else  startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                 }
                 else {
                     if (!flag) {
                         ToastUtils toastUtils = new ToastUtils();
                         toastUtils.show(getApplicationContext(), "密码或用户名错误");
                     }
                 }
            }
        });
    }
    public void doRegister(View view){
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }

    public void doForget(View view){
        if (!admin) {
            admin=true;
            TextView textView=(TextView)findViewById(R.id.forget3);
            textView.setText("用户登录");
        }
        else {
            admin=false;
            TextView textView=(TextView)findViewById(R.id.forget3);
            textView.setText("管理员登录");
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE); SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
        ActivityManager exitM = ActivityManager.getInstance();
        exitM.exit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}