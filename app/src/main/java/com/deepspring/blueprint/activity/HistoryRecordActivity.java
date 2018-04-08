package com.deepspring.blueprint.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.maps.model.LatLng;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.adapter.DbAdapter;
import com.deepspring.blueprint.adapter.ShareRecordAdapter;
import com.deepspring.blueprint.base.BaseActivity;
import com.deepspring.blueprint.bean.PathRecord;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryRecordActivity extends AppCompatActivity {

    private ShareRecordAdapter mAdapter;
    private ListView record_list;
    private DbAdapter DbHepler;
    private Cursor mCursor;
    private Toolbar mToolbar;
    private ArrayList<PathRecord> listdata = new ArrayList<PathRecord>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyrecord);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        record_list = findViewById(R.id.record_list);
        DbHepler=new DbAdapter(this);
        DbHepler.open();
        addRecorddata();
        mAdapter = new ShareRecordAdapter(this,listdata);
        record_list.setAdapter(mAdapter);
        record_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(position);
                Intent intent = new Intent(HistoryRecordActivity.this, ShowRecordActivity.class);
                intent.putExtra("record_item", recorditem);
                startActivity(intent);
            }
        });
    }



    private void addRecorddata() {
        mCursor=DbHepler.getallrecord();
        while (mCursor.moveToNext()) {
            PathRecord record = new PathRecord();
            record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_LINE));
            record.setPathline(parseLocations(lines));
            record.setStartpoint(parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_END))));
            listdata.add(record);
            Log.e("TAG",record.getDate());
        }
        Collections.reverse(listdata);
    }
    public static ArrayList<LatLng> parseLocations(String latLonStr) {
        ArrayList<LatLng> latLonPoints = new ArrayList<LatLng>();
        String[] latLonStrs = latLonStr.split(";");
        for (int i = 0; i < latLonStrs.length; i++) {
            latLonPoints.add(parseLocation(latLonStrs[i]));
        }
        return latLonPoints;
    }

    public static LatLng parseLocation(String latLonStr) {
        if (latLonStr == null || latLonStr.equals("") || latLonStr.equals("[]")) {
            return null;
        }
        double lat = 0.0;
        double lon = 0.0;
        String[] loc = latLonStr.split(",");
        if (loc.length != 2) {
            return null;
        }
        lat = Double.parseDouble(loc[0]);
        lon = Double.parseDouble(loc[1]);
        return new LatLng(lat, lon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
