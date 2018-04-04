package com.deepspring.blueprint.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.adapter.DbAdapter;
import com.deepspring.blueprint.bean.PathRecord;
import com.deepspring.blueprint.utils.TimeUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 不需要drawer，直接继承AC
 * todo: bug 右滑解锁失效 颜色失效 图标大小异常 该功能需要重做 ; 2 定位失败 错误7 key错误( 优先级1 )
 * todo: 完善运动记录页面,添加查询附近共享单车。优先级1.5
 */
public class RunActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, View.OnClickListener{

    private MapView mMapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private PathRecord record;//记录，需要关联user和bottom sheet
    private PolylineOptions mPolyoptions;
    private DbAdapter DbHepler;
    private ImageView iv_pause, iv_continue, iv_end, iv_lock;
    private TextView tv_speed,tv_time,tv_distance;
    private long starttime;
    private long endtime;
    private boolean isStop = false;
    private boolean isPause = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        initViews();
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();//amap
        initpolyline();
        if (record != null) {
            record = null;
        }
        record = new PathRecord();
        starttime = System.currentTimeMillis();
        record.setDate(getcueDate(starttime));
        myTimer.start();
    }

    private void initViews() {
        iv_pause = findViewById(R.id.iv_pause);
        iv_continue = findViewById(R.id.iv_continue);
        iv_end = findViewById(R.id.iv_end);
        iv_lock = findViewById(R.id.iv_lock);

        tv_speed = findViewById(R.id.tv_speed);
        tv_distance = findViewById(R.id.tv_distance);
        tv_time = findViewById(R.id.tv_time);

        iv_pause.setOnClickListener(this);
        iv_continue.setOnClickListener(this);
        iv_end.setOnClickListener(this);
        iv_lock.setOnClickListener(this);
    }

    /**
     * 初始化地图 todo 如果耗时可以开启子线程( 优先级1.5 测试中)
     */
    private void init(){
        if(aMap == null){
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置地图属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.animateCamera(CameraUpdateFactory.zoomTo(16f));
    }

    /**
     * trace属性 todo 样式可调试 ( 优先级2.5 )
     */
    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(15f);
        mPolyoptions.color(Color.BLUE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
//        mLocationClient.stopLocation();//停止定位
//        mLocationClient.onDestroy();//销毁定位客户端。
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        Log.e("TAG", "active start location");
        startlocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 官方demo todo: 需要使用算法筛选( 优先级1.5 )
     */
    private void startlocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置是否强制刷新WIFI，默认为强制刷新
            //mLocationOption.setWifiActiveScan(true);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 开启计时线程,处理定时器
     */
    private int second;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    second++;
                    tv_time.setText(TimeUtil.getTime(second));
                    break;
            }
        }
    };

    Thread myTimer = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!isStop){
                if(!isPause){
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        Log.e("TAG","Timer Thread Error");
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        }
    });

    /**
     * 距离
     */
    private int index = 0;
    private double distance = 0.0;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                //更新distance并且显示到textview
                int size = record.getPathline().size();
                if(size>1){
                    LatLng firstpoint = record.getPathline().get(size-2);
                    LatLng secoundpoint = record.getPathline().get(size-1);
                    if(!isPause && !isStop){
                        distance = distance
                                + AMapUtils.calculateLineDistance(firstpoint,
                                secoundpoint);
                    }
                    //显示
                    DecimalFormat df = new DecimalFormat("0.000");
                    tv_distance.setText(df.format(distance/1000));
                    if(distance < Double.MIN_VALUE || second/distance>1200){
                        tv_speed.setText("00''00'");
                    }else{
                        tv_speed.setText(TimeUtil.getSd(second, distance));
                    }
                }
                record.addpoint(mylocation);
                mPolyoptions.add(mylocation);
                redrawline();//需要用算法调试的地方！
                index++;
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("Retrun Error", errText);
            }
        }
    }

    /**
     * 缺少筛选
     */
    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 0) {
            aMap.clear(true);
            aMap.addPolyline(mPolyoptions);
        }
    }
    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause:
                //开启动画,停止计时，停止画线
                isPause = true;
                startAnimator();
                break;
            case R.id.iv_continue:
                //关闭动画，继续计时，继续划线
                isPause = false;
                endAnimator();
                break;
            case R.id.iv_end:
                isPause = true;
                endtime = System.currentTimeMillis();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认完成此次锻炼？");
                builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isStop = true;
                       //todo ToastUtils.showToast(getBaseContext(),tv_distance+"里程",0);
                        saverecord(record);
                        PathRecord today_record = getTodayData();
//     todo 跳转                   Intent intent = new Intent(getBaseContext(),RecordShowActivity.class);
//                        intent.putExtra("recorditem",today_record);
//                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("继续锻炼", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isPause = false;
                    }
                });
                builder.create().show();
                break;
            case R.id.iv_lock:
                startActivity(new Intent(this,LockActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 记录路径
     * @param record
     */
    protected void saverecord(PathRecord record) {
        if (record != null && record.getPathline().size() > 0) {
            DbHepler = new DbAdapter(this);
            DbHepler.open();
            record.setDuration(String.valueOf((endtime - starttime) / 1000f));
            float distance = 0;
            String pathline = "";
            for (int i = 0; i < record.getPathline().size(); i++) {
                if (i < record.getPathline().size() - 1) {
                    LatLng firstpoint = record.getPathline().get(i);
                    LatLng secoundpoint = record.getPathline().get(i + 1);
                    distance = distance
                            + AMapUtils.calculateLineDistance(firstpoint,
                            secoundpoint);
                }
                LatLng point = record.getPathline().get(i);
                pathline = pathline + point.latitude + "," + point.longitude
                        + ";";
            }
            record.setDistance(String.valueOf(distance));
            record.setStartpoint(record.getPathline().get(0));
            record.setAveragespeed(String.valueOf(distance
                    / (float) (endtime - starttime)));
            record.setEndpoint(record.getPathline().get(
                    record.getPathline().size() - 1));

            String stratpoint = record.getStartpoint().latitude + ","
                    + record.getStartpoint().longitude;
            String endpoint = record.getEndpoint().latitude + ","
                    + record.getEndpoint().longitude;
            DbHepler.createrecord(record.getDistance(), record.getDuration(),
                    record.getAveragespeed(), pathline, stratpoint, endpoint,
                    record.getDate());
            DbHepler.close();
        } else {
            Toast.makeText(this, "没有记录到路径", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private PathRecord getTodayData() {
        PathRecord path = new PathRecord();
        DbHepler.open();
        Cursor mCursor = DbHepler.getallrecord();
        mCursor.moveToLast();
        record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
        record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
        record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
        String lines = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_LINE));
        //todo record.setPathline(RecordActivity.parseLocations(lines));
       // record.setStartpoint(RecordActivity.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_STRAT))));
       // record.setEndpoint(RecordActivity.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_END))));
        return  record;
    }

    private ObjectAnimator animator1;
    private ObjectAnimator animator2;
    private void startAnimator() {
        iv_pause.setVisibility(View.GONE);
        iv_continue.setVisibility(View.VISIBLE);
        iv_end.setVisibility(View.VISIBLE);
        int x1 = -150,x2 = 150;
        PropertyValuesHolder px = PropertyValuesHolder.ofFloat("translationX", (float)x1);
        animator1 = ObjectAnimator.ofPropertyValuesHolder(iv_continue, px);
        animator1.setDuration(200).start();

        PropertyValuesHolder px2 = PropertyValuesHolder.ofFloat("translationX", (float)x2);
        animator2 = ObjectAnimator.ofPropertyValuesHolder(iv_end, px2);
        animator2.setDuration(200).start();
    }
    private ObjectAnimator animator3;
    private ObjectAnimator animator4;
    private void endAnimator() {
        int x1 = 0,x2 = 0;
        PropertyValuesHolder px = PropertyValuesHolder.ofFloat("translationX", (float)x1);
        animator3 = ObjectAnimator.ofPropertyValuesHolder(iv_continue, px);
        animator3.setDuration(300).start();

        PropertyValuesHolder px2 = PropertyValuesHolder.ofFloat("translationX", (float)x2);
        animator4 = ObjectAnimator.ofPropertyValuesHolder(iv_end, px2);
        animator4.setDuration(300).start();
        animator4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_continue.setVisibility(View.GONE);
                iv_end.setVisibility(View.GONE);
                iv_pause.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
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