package com.deepspring.blueprint.activity.step;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.server.StepService;
import com.deepspring.blueprint.step.UpdateUiCallBack;
import com.deepspring.blueprint.utils.SharedPreferencesUtils;
import com.deepspring.blueprint.view.MyStepView;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;


public class StepActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_data, tv_set, tv_isSupport;
    private MyStepView mStepView;
    private SharedPreferencesUtils sp;
    private boolean isBind = false;
    private Toolbar mToolbar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_step;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tv_data = findViewById(R.id.tv_data);
        mStepView = findViewById(R.id.progress);
        tv_set = findViewById(R.id.tv_set);
        tv_isSupport = findViewById(R.id.tv_isSupport);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initDatas();
        addListener();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        sp = new SharedPreferencesUtils(this);
        //获取计划步数，默认6000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "6000");
        mStepView.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
        setupService();
    }

    private void addListener() {
        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "6000");
            mStepView.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "6000");
                    mStepView.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                toastShort("功能待开发");
                //startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:
                startActivity(new Intent(this, HistroyStepActivity.class));
                break;
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
