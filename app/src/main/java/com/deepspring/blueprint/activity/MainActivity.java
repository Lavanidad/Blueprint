package com.deepspring.blueprint.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.fragment.DailyFragment;
import com.deepspring.blueprint.fragment.MapFragment;
import com.deepspring.blueprint.fragment.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.activity_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private DailyFragment mDailyFragment;
    private MapFragment mMapFragment;
    private UserFragment mUserFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    //testtest111111

    @Override
    protected void initViews(){
        super.initViews();
        InitNavigationBar();
        ButterKnife.bind(this);
    }

    private void InitNavigationBar() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.daily_32dp, "发现").setActiveColorResource(R.color.bottom_bg_color))
                .addItem(new BottomNavigationItem(R.mipmap.run_32dp, "运动").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.user_32dp, "我的").setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(1)
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mDailyFragment == null) {
                    mDailyFragment = DailyFragment.newInstance();
                }
                transaction.replace(R.id.activity_container, mDailyFragment);
                break;
            case 1:
                if (mMapFragment == null) {
                    mMapFragment = MapFragment.newInstance();
                }
                transaction.replace(R.id.activity_container, mMapFragment);
                break;
            case 2:
                if (mUserFragment == null) {
                    mUserFragment = UserFragment.newInstance();
                }
                transaction.replace(R.id.activity_container, mUserFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("onTabUnselected", "onTabUnselected: " + position);
    }

    @Override
    public void onTabReselected(int position) {
        Log.d("onTabReselected", "onTabReselected: " + position);
    }

}
