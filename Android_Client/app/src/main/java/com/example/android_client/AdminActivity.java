package com.example.android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_client.bean.ActivityManager;
import com.example.android_client.bean.ChessTable;
import com.example.android_client.bean.ToastUtils;
import com.example.android_client.bean.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class AdminActivity extends AppCompatActivity {

    private final static String TAG=MainActivity.class.getName();

    private EditText findName;
    private Button adminAdd,adminFind;
    private ListView UserList;
    private ArrayAdapter arrayAdapter;

    private List<String> list;   //姓名列表
    private Vector<User> users;    //用户列表
    private User user;

    private boolean flag=false;    //判断数据是否已获取成功
    private String url;   //链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findName = ((EditText) findViewById(R.id.adminUserNameEdit));
        adminAdd = ((Button) findViewById(R.id.adminAdd));
        adminFind = ((Button) findViewById(R.id.adminFind));
        UserList = ((ListView) findViewById(R.id.UserList));
        list = new ArrayList<>();
        users=new Vector<User>();

        ActivityManager exitM = ActivityManager.getInstance();
        exitM.addActivity(AdminActivity.this);

        //add 按钮
        adminAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                //为对话框设置自定义的布局
                final View view = View.inflate(AdminActivity.this,R.layout.edit_user,null);
                builder.setTitle("          添加用户信息"); builder.setView(view);
                final TextView editId = view.findViewById(R.id.Editor_userId);
                final EditText editName = view.findViewById(R.id.Editor_userName);
                final EditText editPassword = view.findViewById(R.id.Editor_password);

                builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = editName.getText().toString(); String userPassword = editPassword.getText().toString();
                        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userPassword)){
                              add(new User(userName,userPassword));
                              adminFind.performClick();//刷新列表
                        }else {
                            Toast.makeText(AdminActivity.this,"用户名或密码不能为空!",Toast.LENGTH_SHORT).show();
                            closeDialog(dialog,false);
                        }
                    }
                });
                builder.setNegativeButton("取消",null);builder.create().show();
            }
        });

        //find 按钮
        adminFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) list.clear();if (!users.isEmpty())users.clear();flag=false;  //清空之前数据
                String username = findName.getText().toString();
                if (!TextUtils.isEmpty(username)) findByName(username);
                else findAll();
                while (true) {
                    if (flag){   //数据获取还未完成
                       arrayAdapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_list_item_1, list);
                       arrayAdapter.notifyDataSetChanged(); UserList.setAdapter(arrayAdapter);
                       break;
                    }
                }
            }
        });

        //查看具体用户信息
        UserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                user=users.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                final View view = View.inflate(AdminActivity.this,R.layout.edit_user,null);
                builder.setTitle("          编辑用户信息"); builder.setView(view);
                final TextView editText1 = view.findViewById(R.id.Editor_userId);editText1.setText(user.id+"");
                final EditText editName = view.findViewById(R.id.Editor_userName);editName.setText(user.name);
                final EditText editPassword = view.findViewById(R.id.Editor_password);editPassword.setText(user.password);
                builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = editName.getText().toString(); String userPassword = editPassword.getText().toString();
                        edit(new User((int)user.id,userName,userPassword));
                        adminFind.performClick();
                    }
                });
                builder.setNegativeButton("取消",null); builder.create().show();
            }
        });

        // 长按删除
        UserList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               user=users.get(position);
                new AlertDialog.Builder(AdminActivity.this).setTitle("删除用户").setMessage("你确定要删除用户"+user.name+"吗？")
                        .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete((int)user.id);
                                adminFind.performClick();
                            }
                        })
                        .setNegativeButton("取消",null).create().show();
                 /*
                因为android默认的优先响应ItemClickListener，所以当返回值为false时，
                ItemLongClickListener和ItemClickListener都会响应点击事件，因此应该
                返回true，提高ItemLongClickListener响应的优先级。
                */
                return true;
            }
        });
    }

    //利用反射机制关闭对话框
    public void closeDialog(DialogInterface dialog,boolean flag){
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //当flag为true时关闭对话框，否则不关闭
            field.set(dialog,flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findAll(){
        url= getResources().getText(R.string.url)+"user/findAll";
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();Gson gson=new Gson();
                User[] usersArray=gson.fromJson(content, User[].class);
                if (usersArray.length!=0) {
                    for (int i = 0; i < usersArray.length; i++){
                        users.add(usersArray[i]); list.add(usersArray[i].name);
                    }
                }
                flag=true;
            }
        });
    }

    public void findByName(String userName){
        url= getResources().getText(R.string.url)+"user/findByName/"+userName;
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string(); Gson gson=new Gson();
                User[] usersArray=gson.fromJson(content, User[].class);
                if (usersArray.length!=0) {
                    for (int i = 0; i < usersArray.length; i++) {
                        users.add(usersArray[i]); list.add(usersArray[i].name);
                    }
                }
                flag=true;
            }
        });
    }

    public void edit(User user){
        String username=user.name;
        String password=user.password;
        if (username==null||username.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (password==null||password.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        url= MessageFormat.format(getResources().getText(R.string.url)+"user/update?id={0}&name={1}&password={2}",user.id,username,password);
        Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG,"failure",e);
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string(); Gson gson=new Gson();
                Map<String ,Object> result=gson.fromJson(content, Map.class);
                double code_=(double) result.get("code"); int code=new Double(code_).intValue();
                if (code==200){
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"修改成功");
                }
                else {
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"该用户名已存在");
                }
            }
        });
    }

    public void add(User user){
        String username=user.name; String password=user.password;
        if (username==null||username.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        if (password==null||password.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        url= MessageFormat.format(getResources().getText(R.string.url)+"//user/save?name={0}&password={1}",username,password);
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
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"添加成功");
                }
                else {
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"该用户名已存在");
                }
            }
        });
    }

    public void delete(int id){
        url= MessageFormat.format(getResources().getText(R.string.url)+"//user/deleteById/{0}",id);
        final Request request=new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils=new ToastUtils();
                toastUtils.show(getApplicationContext(),"网络连接失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string(); Gson gson=new Gson();
                Map<String ,Object> result=gson.fromJson(content, Map.class);
                double code_=(double) result.get("code"); int code=new Double(code_).intValue();
                if (code==200){
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"删除成功");
                }
                else{
                    ToastUtils toastUtils=new ToastUtils();
                    toastUtils.show(getApplicationContext(),"删除失败");
                }
            }
        });
    }

    public void click1(View view ){

    }

    public void init(View view ){
        url = getResources().getText(R.string.url) + "chessTable/initAll" ;
        final Request request = new Request.Builder().url(url).build();
        MainActivity.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ToastUtils toastUtils = new ToastUtils();
                toastUtils.show(getApplicationContext(), "网络连接失败");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ToastUtils toastUtils = new ToastUtils();
                toastUtils.show(getApplicationContext(), "所有棋局初始化成功");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}