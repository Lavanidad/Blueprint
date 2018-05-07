package com.deepspring.blueprint.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.bean.PathRecord;


public class ShowRecordActivity extends BaseActivity implements AMap.OnMapLoadedListener {
    private MapView mapView;
    private AMap aMap;
    private PathRecord mrecord;
    private Marker mMarker;
    private Toolbar mToolbar;
    private final static int UPDATE_MARKER = 0;
    private final static int MOVE_FINISH = 1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_showrecord;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        initViews();
        init();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mapView = findViewById(R.id.map);
        mrecord = getIntent().getParcelableExtra("record_item");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.animateCamera(CameraUpdateFactory.zoomTo(16f));
            aMap.getUiSettings().setTiltGesturesEnabled(true);
            aMap.getUiSettings().setRotateGesturesEnabled(true);
            aMap.getUiSettings().setZoomControlsEnabled(false); //缩放按钮的显示与隐藏
            aMap.getUiSettings().setCompassEnabled(false); //指南针的显示与隐藏
            aMap.getUiSettings().setScaleControlsEnabled(false); //比例尺的显示与隐藏
            aMap.setOnMapLoadedListener(this);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_MARKER:
                    //更新marker的位置
                    LatLng latLng = (LatLng) msg.obj;
                    mMarker.setPosition(latLng);
                    break;
                case MOVE_FINISH:
                    break;
                default:
                    break;
            }
        }

    };

    private LatLngBounds getBounds(){
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mrecord.getPathline().size(); i++) {
            b.include(mrecord.getPathline().get(i));
        }
        return b.build();

    }
    @Override
    public void onMapLoaded() {
        if (mrecord != null && mrecord.getPathline().size()>0) {
            aMap.addPolyline(new PolylineOptions().color(Color.BLUE)
                    .addAll(mrecord.getPathline()));
            aMap.addMarker(new MarkerOptions().position(mrecord.getStartpoint())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
            aMap.addMarker(new MarkerOptions().position(mrecord.getEndpoint())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
            try {
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 50));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
           //logger
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
