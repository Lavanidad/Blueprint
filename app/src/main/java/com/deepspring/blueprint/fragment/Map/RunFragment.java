package com.deepspring.blueprint.fragment.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
        View rootView = inflater.inflate(R.layout.fragment_run,container,false);
        mButton = rootView.findViewById(R.id.run_fab);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic(R.raw.begin);
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        startActivity(new Intent(getActivity(),ReadyActivity.class));
                        return false;
                    }
                }).sendEmptyMessageDelayed(0,1200);
            }
        });
        return rootView;
    }


    private void PlayMusic(int MusicId) {
        music = MediaPlayer.create(getActivity(), MusicId);
        music.start();
    }

}

