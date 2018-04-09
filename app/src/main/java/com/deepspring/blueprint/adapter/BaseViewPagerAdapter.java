package com.deepspring.blueprint.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.deepspring.blueprint.fragment.Map.CycleFragment;
import com.deepspring.blueprint.fragment.Map.RunFragment;


public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTilte;

    public BaseViewPagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);
        this.tabTilte = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RunFragment();//跑步 fragment
            case 1:
                return new CycleFragment();//骑行 fragment
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTilte.length;
    }

}
