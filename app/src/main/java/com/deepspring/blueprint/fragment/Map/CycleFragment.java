package com.deepspring.blueprint.fragment.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.ReadyActivity;
import com.deepspring.blueprint.activity.ReadyScanActivity;
import com.github.edsergeev.TextFloatingActionButton;


public class CycleFragment extends Fragment {

    private TextFloatingActionButton mButton;
    private TextFloatingActionButton mCycleBtn;

    public CycleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cycle,container,false);
        mButton = rootView.findViewById(R.id.cycle_fab);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "功能待开发", Toast.LENGTH_SHORT).show();
            }
        });

        mCycleBtn = rootView.findViewById(R.id.cycle_fab2);
        mCycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(),ReadyScanActivity.class));
            }
        });

        return rootView;
    }
}
