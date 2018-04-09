package com.deepspring.blueprint.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout mRootView;
    private Toast mToast;
    protected FrameLayout flActivityContainer;

    private static final int PERMISSIONS_REQUEST_READ = 1;
    private static final int PERMISSIONS_REQUEST_LOCATION = 2;

    private final static String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    List<String> mPermissionList = new ArrayList<>();


    private NavigationView.OnNavigationItemSelectedListener mListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_github:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Lavanidad")));
                    break;
                case R.id.nav_wechat:
                    toastShort("wechat clicked");
                    break;
                case R.id.nav_about:
                   toastShort("about clicked");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initViews();
        initPermissions();
        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initPermissions() {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(BaseActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            //Toast.makeText(BaseActivity.this,"已经授权",Toast.LENGTH_LONG).show();
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(BaseActivity.this, permissions,PERMISSIONS_REQUEST_LOCATION );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                toastShort("权限已拒绝");
            }
        } else if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        toastShort("缺少必要权限");
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected void initDatas() {

    }

    /**
     * 初始化view,在 initDatas 后调用
     */
    protected void initViews() {
            flActivityContainer = findViewById(R.id.activity_container);
            flActivityContainer.addView(LayoutInflater.from(this).inflate(getLayoutId(), flActivityContainer, false));
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(mListener);
            mRootView = findViewById(R.id.root_layout);
    }



    /**
     * 发出一个短Toast
     *
     * @param text 内容
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 发出一个长toast提醒
     *
     * @param text 内容
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }

    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, duration);
                    } else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }
}
