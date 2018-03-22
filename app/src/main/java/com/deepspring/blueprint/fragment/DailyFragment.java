package com.deepspring.blueprint.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepspring.blueprint.R;

import java.lang.reflect.Field;


public class DailyFragment extends Fragment {

    public DailyFragment() {
        // Required empty public constructor
    }


    public static DailyFragment newInstance(String title) {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

}
