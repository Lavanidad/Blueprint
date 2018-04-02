package com.deepspring.blueprint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deepspring.blueprint.R;


public class UserFragment extends Fragment {

    private Button mButton;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String title) {
        UserFragment fragment = new UserFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        mButton = rootView.findViewById(R.id.map_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),
                       com.amap.api.maps.offlinemap.OfflineMapActivity.class));
            }
        });//todo 离线下载异常
        return rootView;
    }

}
