package com.deepspring.blueprint.activity;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.deepspring.blueprint.R;
import com.tandong.switchlayout.SwichLayoutInterFace;
import com.tandong.switchlayout.SwitchLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadyActivity extends AppCompatActivity implements Animation.AnimationListener,SwichLayoutInterFace {

    @BindView(R.id.iv_number)
    ImageView iv1;
    @BindView(R.id.iv_number2)
    ImageView iv2;
    @BindView(R.id.iv_number3)
    ImageView iv3;

    private MediaPlayer music = null;
    private Animation animation1, animation2, animation3;
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        setEnterSwichLayout();
        ButterKnife.bind(this);
        animation1 = AnimationUtils.loadAnimation(this,R.anim.img_scale);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.img_scale);
        animation3 = AnimationUtils.loadAnimation(this,R.anim.img_scale);
        animation1.setDuration(1000);
        animation2.setDuration(1000);
        animation3.setDuration(1000);
        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);
        animation3.setAnimationListener(this);
        iv3.startAnimation(animation3);
        PlayMusic(R.raw.t3);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        index++;
        switch (index){
            case 1:
                PlayMusic(R.raw.t2);
                iv3.clearAnimation();
                iv3.setVisibility(View.GONE);
                iv2.setVisibility(View.VISIBLE);
                iv2.startAnimation(animation2);
                break;
            case 2:
                PlayMusic(R.raw.t1);
                iv2.clearAnimation();
                iv2.setVisibility(View.GONE);
                iv1.setVisibility(View.VISIBLE);
                iv1.startAnimation(animation1);
                break;
            case 3:

                iv1.clearAnimation();
                iv1.setVisibility(View.GONE);
                startActivity(new Intent(ReadyActivity.this,RunActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void PlayMusic(int MusicId) {
        music = MediaPlayer.create(this, MusicId);
        music.start();
    }

    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.ScaleBig(this, false, null);
    }

    @Override
    public void setExitSwichLayout() {
        SwitchLayout.ScaleBig(this, false, null);
    }

    @Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        switch(event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                return false;
        }
        return true;
    }
}
