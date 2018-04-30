package com.deepspring.blueprint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class UserFragment extends Fragment {

    private Button mButton;
    private Button LoginBtn;

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
      //  EventBus.getDefault().register(this);//在当前界面注册一个订阅者
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
        });
        LoginBtn = rootView.findViewById(R.id.fragment_login);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

}
