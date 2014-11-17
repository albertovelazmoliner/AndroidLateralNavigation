package com.fieldaware.viewpagerfragmentstate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17/11/14.
 */
public class LateralNavigationAdapter extends FragmentPagerAdapter {

    boolean mTwoPane = false;
    public List<Fragment> mFragments = new ArrayList<Fragment>();

    public LateralNavigationAdapter(FragmentManager fm, boolean twoPane) {
        super(fm);
        this.mTwoPane = twoPane;
    }

    @Override
    public int getItemPosition(Object object) {
        if ((getCount()==1 && mTwoPane) || (getCount()==0 && !mTwoPane)) {
            return PagerAdapter.POSITION_NONE;
        }
        return PagerAdapter.POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public float getPageWidth(int position) {
        if (mTwoPane) {
            return(0.5f);
        } else {
            return(1.0f);
        }
    }

    public void addFragment(int position){
        Fragment fr = ArrayListFragment.newInstance(position);
        mFragments.add(fr);
    }

    public void removeLastFragment(){
        mFragments.remove(mFragments.size()-1);
    }
}
