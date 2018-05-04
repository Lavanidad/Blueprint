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
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.newim.event.MessageEvent;


public class UserFragment extends Fragment {

    private Button mButton;
    private Button LoginBtn;
    private View unLoginView;
    private View LoginView;
    private boolean isLogin = false;

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
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
    }

    //用于接收传MessageEvent这个类的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(MessageEvent messageEvent) {
        isLogin = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        unLoginView = rootView.findViewById(R.id.userinfo_head);
        LoginView = rootView.findViewById(R.id.userinfo_head_logined);
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
        changeView();
        return rootView;
    }

    public void changeView(){
        if(isLogin = true){
            unLoginView.setVisibility(View.GONE);
            LoginView.setVisibility(View.VISIBLE);
        }else {
            unLoginView.setVisibility(View.VISIBLE);
            LoginView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

}
