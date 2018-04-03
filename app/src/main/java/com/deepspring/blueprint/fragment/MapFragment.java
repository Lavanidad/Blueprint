package com.deepspring.blueprint.fragment;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cocosw.bottomsheet.BottomSheet;
import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.step.StepActivity;
import com.deepspring.blueprint.adapter.BaseViewPagerAdapter;
import com.deepspring.blueprint.base.BaseFragment;


public class MapFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private String[] tabTitle = {"跑步","骑行"};
    private FloatingActionButton mFloatButton;
    private boolean fbOpend = false;


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
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initViews(rootView);
        Button mStepBtn = rootView.findViewById(R.id.step_countBtn);
        mStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StepActivity.class);
                startActivity(intent);
            }
        });
        mFloatButton = rootView.findViewById(R.id.flo_btn);
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fbOpend){
                    openBtn(v);
                }else{
                    closeBtn(v);
                }
                new BottomSheet.Builder(getActivity()).sheet(R.menu.bottom_sheet).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.history:
                                toast("跳转");
                                break;
                            case R.id.body:
                                toast("跳转");
                                break;

                        }
                    }
                }).show();
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

    public void openBtn(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        animator.setDuration(500);
        animator.start();
        fbOpend = true;
    }
    public void closeBtn(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();
        fbOpend = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
