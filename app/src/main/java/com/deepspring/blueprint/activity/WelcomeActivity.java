package com.deepspring.blueprint.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;


public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                WelcomeActivity.this.finish();
            }
        },1500);
    }
}
