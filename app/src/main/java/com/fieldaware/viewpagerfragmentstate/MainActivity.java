package com.fieldaware.viewpagerfragmentstate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {
    LateralNavigationAdapter mAdapter;
    CustomViewPager mPager;
    int panelSelected = 0;
    boolean twoPanel = false;
    final static String TAG = "Puta mierda";
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {}

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                //if(getSupportFragmentManager().getFragments().size()<mAdapter.mFragments.size()) {
                    updatePager();
                    Log.d(TAG,"---------------------------");
                    Log.d(TAG,"Adjust fragments number");
                    Log.d(TAG,"---------------------------");
                //}

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setEnabledSwipe(false);
        //twoPanel = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        float w = getResources().getDisplayMetrics().widthPixels;
        float h = getResources().getDisplayMetrics().heightPixels;
        twoPanel = (w > h) ? true : false;
        mAdapter = new LateralNavigationAdapter(getSupportFragmentManager(), twoPanel);

        if (savedInstanceState != null) {
            panelSelected = savedInstanceState.getInt("panelSelected");
        }

        if (getSupportFragmentManager().getFragments() != null){
            for (int i=0; i<getSupportFragmentManager().getFragments().size();i++) {
                mAdapter.addFragment(i);
            }
        } else {
            mAdapter.addFragment(0);
        }
        mAdapter.notifyDataSetChanged();
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(mListener);
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");
        outState.putInt("panelSelected", panelSelected);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        mAdapter.mTwoPane = !mAdapter.mTwoPane;
        updatePager();
    }



    public void onListItemClick(ListView l, View v, int position, long id) {
        boolean exception = false;
        panelSelected++;
        if (panelSelected==2 && twoPanel) {
            mPager.setCurrentItem(panelSelected - 2);
        }
        mAdapter.addFragment(panelSelected);
        mAdapter.notifyDataSetChanged();
        try{
            if ((panelSelected > 2 && twoPanel) || !twoPanel || ((panelSelected == 1 && twoPanel))) {
                mPager.setCurrentItem(panelSelected, true);
            }
            if (panelSelected==2 && twoPanel) {
                mPager.setCurrentItem(panelSelected, true);
            }
        } catch (Exception e) {
            mAdapter.notifyDataSetChanged();
            Log.d(TAG,"---------------------------");
            Log.d(TAG,"It as been exception!!!!!!!");
            Log.d(TAG,"---------------------------");
            exception = true;
        } finally {
            if (exception){
                if ((panelSelected > 2 && twoPanel) || !twoPanel || ((panelSelected == 1 && twoPanel))) {
                    mPager.setCurrentItem(panelSelected, true);
                }
                if (panelSelected==2 && twoPanel) {
                    mPager.setCurrentItem(panelSelected, true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(panelSelected>1) {
            mPager.setCurrentItem((twoPanel) ? panelSelected - 2 : panelSelected - 1, true);
            getSupportFragmentManager().getFragments().remove(panelSelected);
            mAdapter.removeLastFragment();
            mAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
            panelSelected--;
        } else if (panelSelected>0) {
            mPager.setCurrentItem(0);
            getSupportFragmentManager().getFragments().remove(1);
            mAdapter.removeLastFragment();
            mAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
            panelSelected--;
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    public void updatePager() {
        mPager.setAdapter(null);
        mPager.setAdapter(mAdapter);
        Log.d(TAG,"Panel selected in updatePager: "+panelSelected);
        mPager.setCurrentItem(panelSelected,true);
    }
}
