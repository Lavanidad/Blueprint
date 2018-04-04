package com.deepspring.blueprint.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.bean.PathRecord;

import java.util.ArrayList;

public class ShareRecordAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<PathRecord> list;

    public ShareRecordAdapter(Context context, ArrayList<PathRecord> list) {
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.share_item, null);
            holder.date = convertView.findViewById(R.id.date);
            holder.record = convertView.findViewById(R.id.record);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PathRecord item = list.get(position);
        holder.date.setText(item.getDate());
        holder.record.setText(item.toString());
        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView record;
    }
}
