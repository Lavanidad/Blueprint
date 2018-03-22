package com.deepspring.blueprint.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.adapter.BaseViewPagerAdapter;
import com.deepspring.blueprint.base.BaseFragment;
import com.deepspring.blueprint.base.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MapFragment extends Fragment {
    private Context mContext;
    private TabLayout mTabNav;
    private ViewPager mViewPager;

    public MapFragment() {
    }

    public static MapFragment newInstance(String title) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }


    protected void initViews(ViewHolder holder, View root) {
        mTabNav = holder.get(R.id.tab_nav);
        mViewPager = holder.get(R.id.viewpager);
        Toolbar mToolbar = holder.get(R.id.toolbar);
        mToolbar.setTitle("蓝图");
        ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(mContext, getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabNav.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
