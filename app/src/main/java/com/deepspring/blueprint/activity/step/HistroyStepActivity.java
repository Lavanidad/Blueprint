package com.deepspring.blueprint.activity.step;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.adapter.CommonAdapter;
import com.deepspring.blueprint.adapter.CommonViewHolder;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.step.bean.StepData;
import com.deepspring.blueprint.utils.DbUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

public class HistroyStepActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ListView mListView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_historystep;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initDatas();
        initViews();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        setEmptyView(mListView);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "History_Step");
        }
        List<StepData> stepDatas =DbUtils.getQueryAll(StepData.class);
        Logger.d("stepDatas="+stepDatas);
        mListView.setAdapter(new CommonAdapter<StepData>(this,stepDatas,R.layout.step_history_item) {
            @Override
            protected void convertView(View item, StepData stepData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.tv_data);
                TextView tv_step= CommonViewHolder.get(item,R.id.tv_step);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep()+"步");
            }
        });
    }

    @Override
    protected void initViews() {
        super.initViews();
        mListView = findViewById(R.id.list_step);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
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
