package com.deepspring.blueprint.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.fragment.DailyFragment;
import com.deepspring.blueprint.fragment.MapFragment;
import com.deepspring.blueprint.fragment.UserFragment;
import com.deepspring.blueprint.server.StepService;
import com.deepspring.blueprint.step.UpdateUiCallBack;


import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

//todo-list  1、计步页面toolbar添加返回 2、map 3、图形优化，bitmap压缩 4、第一页
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.activity_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private static int LAST_SELECTED_POSITION = 0;
    private DailyFragment mDailyFragment;
    private MapFragment mMapFragment;
    private UserFragment mUserFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), getColor(R.color.colorPrimary));
            } catch (Exception e) {}
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(Color.BLACK);
            }
        }
        super.initViews();
        InitNavigationBar();
        ButterKnife.bind(this);
    }

        private void InitNavigationBar() {
            BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.mipmap.daily_32dp, "发现").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.mipmap.run_32dp, "运动").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.mipmap.user_32dp, "我的").setActiveColorResource(R.color.colorPrimary))
                    .setFirstSelectedPosition(LAST_SELECTED_POSITION)
                    .setTabSelectedListener(this)
                    .setMode(BottomNavigationBar.MODE_SHIFTING)
                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                    .initialise();//end
            setDefaultFragment();//设置默认
        }

        private void setDefaultFragment() {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.main_container, DailyFragment.newInstance("发现"));
            transaction.commit();
        }

        @Override
        public void onTabSelected(int position) {
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            switch (position) {
                case 0:
                    if (mDailyFragment == null) {
                        mDailyFragment = DailyFragment.newInstance("发现");
                    }
                    transaction.replace(R.id.main_container, mDailyFragment);
                    break;
                case 1:
                    if (mMapFragment == null) {
                        mMapFragment = MapFragment.newInstance("运动");
                    }
                    transaction.replace(R.id.main_container, mMapFragment);
                    break;
                case 2:
                    if (mUserFragment == null) {
                        mUserFragment = UserFragment.newInstance("我的");
                    }
                    transaction.replace(R.id.main_container, mUserFragment);
                    break;
                default:
                    break;
            }
            transaction.commit();// 事务提交
        }

        /**
         * 设置未选中Fragment 事务
         */
        @Override
        public void onTabUnselected(int position) {
        }

        /**
         * 设置释放Fragment 事务
         */
        @Override
        public void onTabReselected(int position) {
            Log.d("onTabReselected", "onTabReselected: " + position);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
