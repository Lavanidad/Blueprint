package com.deepspring.blueprint.activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.bean.LoginSuccessdEvent;
import com.deepspring.blueprint.fragment.UserFragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText etusername, etpassword;
    private Button registerBtn, loginBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        EventBus.getDefault().register(this);
        initViews();
    }
    @Override
    protected void initViews() {
        super.initViews();
        etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.btn_signup);
        registerBtn.setOnClickListener(this);
    }

    @Subscribe          //订阅事件FirstEvent
    public void onEventMainThread(LoginSuccessdEvent event){
        Log.i("TAG","LoginSuccessdEvent");
        LoginActivity.this.finish();//收到订阅事件之后关闭当前界面
        Toast.makeText(this, "finish", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                BmobUser userlogin = new BmobUser();
                userlogin.setUsername(etusername.getText().toString());
                userlogin.setPassword(etpassword.getText().toString());
                userlogin.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, cn.bmob.v3.exception.BmobException e) {
                        if(e==null){
                            toastShort(bmobUser.getUsername()+"登录成功");
                            EventBus.getDefault().post(new LoginSuccessdEvent(true));
                            LoginActivity.this.finish();

                        }else {
                            toastShort("unknowError");
                            Log.e("登录失败", "result: ", e);
                        }
                    }
                });
                break;
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
