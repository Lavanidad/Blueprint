package com.deepspring.blueprint.activity;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;

public class LockActivity extends BaseActivity{
    private ImageView iv_touch;
    private AnimationDrawable animationDrawable;
    private int x;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();
        iv_touch = findViewById(R.id.iv_touch);
        animationDrawable = (AnimationDrawable) iv_touch.getDrawable();
        animationDrawable.start();
    }

    private boolean isOnSlidingUnlock(float x, float y) {
        double deltX =  iv_touch.getX()+ iv_touch.getWidth();
        double deltY = iv_touch.getY() + iv_touch.getHeight();
        if(x>iv_touch.getX() && x<deltX && y>iv_touch.getY() && y<deltY){
            return true;
        }
        return  false;
    }

    boolean flag = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                flag = isOnSlidingUnlock(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if(event.getX()-x > 200){
                    Log.e("TAG","x-->"+x+","+event.getX());
                    if(flag) {
                        animationDrawable.stop();
                        finish();
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
    }

}
