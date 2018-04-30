package com.deepspring.blueprint.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private EditText etusername, etpassword;
    private Button mButton;
    private Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();
        etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        mButton = (Button) findViewById(R.id.btn_register);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        BmobUser bu = new BmobUser();
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();

        bu.setUsername(username);
        bu.setPassword(password);
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    toastLong("注册成功");
                    startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                    }else {
                    toastShort("服务器繁忙，稍后重试");
                    Log.e("signup：","错误原因："+e);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
