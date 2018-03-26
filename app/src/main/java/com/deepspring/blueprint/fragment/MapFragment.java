package com.deepspring.blueprint.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.step.StepActivity;
import com.deepspring.blueprint.adapter.BaseViewPagerAdapter;
import com.deepspring.blueprint.base.BaseFragment;


public class MapFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private String[] tabTitle = {"跑步","骑行"};

    public static MapFragment newInstance(String title) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initViews(rootView);
        Button mStepBtn = rootView.findViewById(R.id.step_countBtn);
        mStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Click StepActivity");
                Intent intent = new Intent(getActivity(),StepActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    protected void initViews(View rootView) {
        mTabLayout = rootView.findViewById(R.id.tab_nav);
        mViewPager = rootView.findViewById(R.id.viewpager);
        mToolbar = rootView.findViewById(R.id.toolbar);
        mToolbar.setTitle("蓝图");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mViewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager(),tabTitle));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
