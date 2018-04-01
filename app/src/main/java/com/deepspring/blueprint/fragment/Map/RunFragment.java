package com.deepspring.blueprint.fragment.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.RunActivity;
import com.github.edsergeev.TextFloatingActionButton;


public class RunFragment extends Fragment {

    private TextFloatingActionButton mButton;

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
                Intent intent = new Intent(getActivity(), RunActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


}
