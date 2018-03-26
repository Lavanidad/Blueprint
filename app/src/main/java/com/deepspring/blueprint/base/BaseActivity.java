package com.deepspring.blueprint.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.deepspring.blueprint.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout mRootView;
    private Toast mToast;
    protected FrameLayout flActivityContainer;

    private NavigationView.OnNavigationItemSelectedListener mListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_github:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Lavanidad")));
                    break;
                case R.id.nav_wechat:
                    toastShort("wechat clicked");
                    break;
                case R.id.nav_about:
                   toastShort("about clicked");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initViews();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected void initDatas() {

    }

    /**
     * 初始化view,在 initDatas 后调用
     */
    protected void initViews() {
        flActivityContainer = findViewById(R.id.activity_container);
        flActivityContainer.addView(LayoutInflater.from(this).inflate(getLayoutId(), flActivityContainer, false));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mListener);
        mRootView = findViewById(R.id.root_layout);
    }



    /**
     * 发出一个短Toast
     *
     * @param text 内容
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 发出一个长toast提醒
     *
     * @param text 内容
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }

    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, duration);
                    } else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }
}
