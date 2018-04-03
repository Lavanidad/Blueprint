package com.deepspring.blueprint.fragment.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.ReadyActivity;
import com.github.edsergeev.TextFloatingActionButton;


public class RunFragment extends Fragment {

    private TextFloatingActionButton mButton;
    private MediaPlayer music = null;

    public RunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initGps();
        View rootView = inflater.inflate(R.layout.fragment_run,container,false);
        mButton = rootView.findViewById(R.id.run_fab);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic(R.raw.begin);
                Intent intent = new Intent(getActivity(), ReadyActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void initGps() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("为了正常记录你的运动数据，我们需要您开启GPS定位功能");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            final Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    PlayMusic(R.raw.begin);
                                    handler.sendEmptyMessageDelayed(1,1500);
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                                }
                            };

                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {

        }
    }

    private void PlayMusic(int MusicId) {
        music = MediaPlayer.create(getActivity(), MusicId);
        music.start();
    }

}

