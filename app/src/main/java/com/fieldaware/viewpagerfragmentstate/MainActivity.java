package com.fieldaware.viewpagerfragmentstate;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends FragmentActivity {
    Fragment leftFragment;
    Fragment rightFragment;
    LateralNavigationAdapter mAdapter;
    CustomViewPager mPager;
    int panelSelected = 0;
    boolean twoPanel = false;
    boolean isEnoughBigToShowAlwaysTwoPanel = false;
    final static String TAG = "FieldAware";
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {}

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                mPager.updatePager(mAdapter, panelSelected);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setEnabledSwipe(false);
        float w = getResources().getDisplayMetrics().widthPixels;
        float h = getResources().getDisplayMetrics().heightPixels;
        twoPanel = (w > h) ? true : false;
        isEnoughBigToShowAlwaysTwoPanel = isDeviceEnoughBigToShowAlwaysTwoPanel();

        if (!isTwoPanelInLandscape()) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            twoPanel = false;
        }
        if (isEnoughBigToShowAlwaysTwoPanel) twoPanel = true;

        mAdapter = new LateralNavigationAdapter(getSupportFragmentManager(), twoPanel);

        if (savedInstanceState != null) {
            panelSelected = savedInstanceState.getInt("panelSelected");
        }

        if (getSupportFragmentManager().getFragments() != null){
            for (int i=0; i<getSupportFragmentManager().getFragments().size();i++) {
                mAdapter.addFragment(i);
            }
        } else {
            mAdapter.addFragment(SimpleListFragment.newInstance());
            leftFragment = mAdapter.mFragments.get(0);
        }
        mAdapter.notifyDataSetChanged();
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(mListener);
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("panelSelected", panelSelected);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isEnoughBigToShowAlwaysTwoPanel && isTwoPanelInLandscape()) {
            mAdapter.mTwoPane = !mAdapter.mTwoPane;
            twoPanel = !twoPanel;
        }
        mPager.updatePager(mAdapter, panelSelected);
    }

    public void onListItemClick(Fragment currentFragment, int position) {
        SimpleListFragment rf = SimpleListFragment.newInstance(position);
        loadNewFragment(currentFragment, rf);
    }

    protected void loadNewFragment(Fragment currentFragment, Fragment newFragment) {
        boolean exception = false;
        if(twoPanel) {
            if (currentFragment == leftFragment) {
                if (rightFragment != null) {
                    try{
                        getSupportFragmentManager().getFragments().remove(panelSelected);
                        mAdapter.removeLastFragment();
                        mAdapter.addFragment(newFragment);
                        mAdapter.notifyDataSetChanged();
                        mPager.setCurrentItem(panelSelected, true);
                    } catch (Exception e) {
                        mAdapter.notifyDataSetChanged();
                    } finally {
                        mPager.updatePager(mAdapter, panelSelected);
                        rightFragment = newFragment;
                    }
                } else {
                    panelSelected++;
                    try{
                        mAdapter.addFragment(newFragment);
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        mAdapter.notifyDataSetChanged();
                    } finally {
                        mPager.updatePager(mAdapter, panelSelected);
                        rightFragment = newFragment;
                    }
                }
            } else {
                exception = false;
                panelSelected++;
                if (panelSelected==2 && twoPanel) {
                    mPager.setCurrentItem(panelSelected - 2);
                }
                mAdapter.addFragment(newFragment);
                mAdapter.notifyDataSetChanged();
                try{
                    mPager.setCurrentItem(panelSelected, true);
                } catch (Exception e) {
                    mAdapter.notifyDataSetChanged();
                    exception = true;
                } finally {
                    if (exception){
                        mPager.setCurrentItem(panelSelected, true);
                    }
                }
                rightFragment = newFragment;
                leftFragment = mAdapter.mFragments.get(mAdapter.mFragments.size()-2);
            }
        } else {
            exception = false;
            panelSelected++;
            mAdapter.addFragment(newFragment);
            mAdapter.notifyDataSetChanged();
            try{
                mPager.setCurrentItem(panelSelected, true);
            } catch (Exception e) {
                mAdapter.notifyDataSetChanged();
                exception = true;
            } finally {
                if (exception){
                    mPager.setCurrentItem(panelSelected, true);
                }
            }
            rightFragment = newFragment;
            leftFragment = mAdapter.mFragments.get(mAdapter.mFragments.size()-2);
        }
    }

    @Override
    public void onBackPressed() {
        if(panelSelected>1) {
            mPager.setCurrentItem((twoPanel) ? panelSelected - 2 : panelSelected - 1, true);
            getSupportFragmentManager().getFragments().remove(panelSelected);
            if (twoPanel) {
                leftFragment = mAdapter.mFragments.get(panelSelected-2);
                rightFragment = mAdapter.mFragments.get(panelSelected-1);
            }
            mAdapter.removeLastFragment();
            mAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
            panelSelected--;
        } else if (panelSelected>0) {
            mPager.setCurrentItem(0);
            if (twoPanel) {
                leftFragment = mAdapter.mFragments.get(0);
                rightFragment = null;
            }
            getSupportFragmentManager().getFragments().remove(1);
            mAdapter.removeLastFragment();
            mAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
            panelSelected--;
        } else {
            super.onBackPressed();
        }
    }

    private double getDiagonalSize(){
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;
        //The size of the diagonal in inches is equal to the square root of the height in inches squared plus the width in inches squared.
        double diagonalInches = Math.sqrt( (widthInches * widthInches) + (heightInches * heightInches));
        return diagonalInches;
    }

    private boolean isDeviceEnoughBigToShowAlwaysTwoPanel(){
        double diagonalInches = getDiagonalSize();
        Log.d(TAG,"Device diagonal inches: "+diagonalInches);
        if (diagonalInches > 7) {
            //Device is a 7" tablet bigger than 7"
            return true;
        }
        return false;
    }

    private boolean isTwoPanelInLandscape(){
        double diagonalInches = getDiagonalSize();
        Log.d(TAG,"Device diagonal inches: "+diagonalInches);
        if (diagonalInches > 6.5) {
            //Device is a 7" tablet bigger than 7"
            return true;
        }
        return false;
    }

}