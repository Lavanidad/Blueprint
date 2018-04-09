package com.deepspring.blueprint.adapter;


import android.util.SparseArray;
import android.view.View;

/**
 * 万能的ViewHolder
 * @author
 */
public class CommonViewHolder {
    /**
     * @param view 所有缓存View的根View
     * @param id   缓存View的唯一标识
     * @return
     */
    public static <T extends View> T get(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);//创建集合和根View关联
        }
        View chidlView = viewHolder.get(id);//获取根View储存在集合中的子View
        if (chidlView == null) {
            chidlView = view.findViewById(id);
            viewHolder.put(id, chidlView);
        }
        return (T) chidlView;
    }
}
