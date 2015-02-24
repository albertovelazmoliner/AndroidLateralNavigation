package com.fieldaware.viewpagerfragmentstate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Alberto Velaz on 17/11/14.
 */
public class LateralNavigationAdapter extends FragmentPagerAdapter {

    public boolean mTwoPane = false;
    private ArrayList<WeakReference<Fragment>> mFragments = new ArrayList<WeakReference<Fragment>>();

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
        WeakReference<Fragment> weakReference = mFragments.get(position);
        return weakReference.get();
    }

    @Override
    public float getPageWidth(int position) {
        if (mTwoPane) {
            return(0.5f);
        } else {
            return(1.0f);
        }
    }

    /*public void addFragment(int position){
        Fragment fr = ArrayListFragment.newInstance(position);
        mFragments.add(fr);
    }*/

    public void addFragment(Fragment fr) {
        mFragments.add(new WeakReference<Fragment>(fr));
        notifyDataSetChanged();

    }

    public void removeLastFragment(){
        mFragments.remove(mFragments.size()-1);
    }

    public void remove(int position){
        mFragments.remove(position);
        notifyDataSetChanged();
    }
}