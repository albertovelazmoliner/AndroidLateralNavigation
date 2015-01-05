package com.fieldaware.viewpagerfragmentstate;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

public class MainActivity extends FragmentActivity {
    Fragment leftFragment;
    Fragment rightFragment;
    LateralNavigationAdapter mAdapter;
    CustomViewPager mPager;
    int panelSelected = 0;
    boolean twoPanel = false;
    boolean isTwoPanelInLandscape = false;
    boolean isEnoughBigToShowAlwaysTwoPanel = false;
    boolean touchable = true;
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {}

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                mPager.updatePager(mAdapter, panelSelected);
                touchable = true;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setEnabledSwipe(false);
        setScreenSettings();

        if (!isTwoPanelInLandscape) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            twoPanel = false;
        } else if (isEnoughBigToShowAlwaysTwoPanel) {
            twoPanel = true;
        }

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
        if (!isEnoughBigToShowAlwaysTwoPanel && isTwoPanelInLandscape) {
            mAdapter.mTwoPane = !mAdapter.mTwoPane;
            twoPanel = !twoPanel;
        }
        mPager.updatePager(mAdapter, panelSelected);
    }

    public void onListItemClick(Fragment currentFragment, int position) {
        if (touchable) {
            SimpleListFragment rf = SimpleListFragment.newInstance(position);
            if (panelSelected>1 && currentFragment != leftFragment) touchable = false;
            loadNewFragment(currentFragment, rf);

        }
    }

    private void loadNewFragment(Fragment currentFragment, Fragment newFragment) {
        if(twoPanel) {
            if (currentFragment == leftFragment) {
                if (rightFragment != null) {
                    getSupportFragmentManager().getFragments().remove(panelSelected);
                    mAdapter.removeLastFragment(); //remove current right panel
                    mAdapter.addFragment(newFragment);
                    mAdapter.notifyDataSetChanged();
                    mPager.setCurrentItem(panelSelected, true);
                    mPager.updatePager(mAdapter, panelSelected);
                } else {
                    panelSelected++;
                    mAdapter.addFragment(newFragment);
                    mAdapter.notifyDataSetChanged();
                    mPager.updatePager(mAdapter, panelSelected);
                }
                rightFragment = newFragment;
            } else {
                updatingPagerAfterAddingFragmentWithAnimation(newFragment);
            }
        } else {
            updatingPagerAfterAddingFragmentWithAnimation(newFragment);
        }
    }

    private void updatingPagerAfterAddingFragmentWithAnimation(Fragment newFragment) {
        panelSelected++;
        if (panelSelected==2 && twoPanel) {
            mPager.setCurrentItem(panelSelected - 2);
        }
        mAdapter.addFragment(newFragment);
        mAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(panelSelected, true);
        rightFragment = newFragment;
        leftFragment = mAdapter.mFragments.get(mAdapter.mFragments.size()-2);
    }

    @Override
    public void onBackPressed() {
        if (panelSelected>0) {
            if(panelSelected>1) {
                mPager.setCurrentItem((twoPanel) ? panelSelected - 2 : panelSelected - 1, true);
            } else {
                mPager.setCurrentItem(0);
            }
            updatingPagerRemovingFragment();
        } else {
            super.onBackPressed();
        }
    }

    private void updatingPagerRemovingFragment() {
        getSupportFragmentManager().getFragments().remove(panelSelected);
        if (panelSelected >1 && twoPanel){
            leftFragment = mAdapter.mFragments.get(panelSelected-2);
            rightFragment = mAdapter.mFragments.get(panelSelected-1);
        } else if (panelSelected ==  1) {
            leftFragment = mAdapter.mFragments.get(0);
            rightFragment = null;
        }
        mAdapter.removeLastFragment();
        mAdapter.notifyDataSetChanged();
        mPager.refreshDrawableState();
        panelSelected--;
    }

    private boolean isTwoPanel(){
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        return (screenWidth > screenHeight);
    }

    private void setScreenSettings(){
        twoPanel = isTwoPanel();
        double diagonalSize = getDiagonalSize();
        isEnoughBigToShowAlwaysTwoPanel = (diagonalSize > 7);
        isTwoPanelInLandscape = (diagonalSize > 6.5);
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
        return Math.sqrt( (widthInches * widthInches) + (heightInches * heightInches));
    }
}