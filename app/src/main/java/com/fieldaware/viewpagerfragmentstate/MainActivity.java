package com.fieldaware.viewpagerfragmentstate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    private static final String[] items= { "lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
            "vel", "erat", "placerat", "ante", "porttitor", "sodales",
            "pellentesque", "augue", "purus" };
    Fragment leftFragment;
    Fragment rightFragment;
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
            //mAdapter.addFragment(0);
            mAdapter.addFragment(SimpleListFragment.newInstance(items));
            leftFragment = mAdapter.mFragments.get(0);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mAdapter.mTwoPane = !mAdapter.mTwoPane;
        twoPanel = !twoPanel;
        updatePager();
    }



    public void onListItemClick(Fragment fragment, int position) {


        ArrayList<String> middleContents=new ArrayList<String>();

        for (int i=0; i < 20; i++) {
            middleContents.add(items[position] + " #" + i);
        }
        SimpleListFragment rf = SimpleListFragment.newInstance(middleContents);

        if(twoPanel) {
            if (fragment == leftFragment) {
                boolean exception = false;

                if (rightFragment != null) {
                    try{
                        getSupportFragmentManager().getFragments().remove(panelSelected);
                        mAdapter.removeLastFragment();
                        mAdapter.addFragment(rf);
                        mAdapter.notifyDataSetChanged();
                        mPager.setCurrentItem(panelSelected, true);

                    } catch (Exception e) {
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG,"----------------------------");
                        Log.d(TAG,"It has been exception!!!!!!!");
                        Log.d(TAG,"----------------------------");
                        exception = true;
                    } finally {
                        updatePager();
                        rightFragment = rf;
                    }
                } else {
                    panelSelected++;
                    try{
                        mAdapter.addFragment(rf);
                        mAdapter.notifyDataSetChanged();
                        //mPager.setCurrentItem(panelSelected, true);

                    } catch (Exception e) {
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG,"---------------------------");
                        Log.d(TAG,"It as been exception!!!!!!!");
                        Log.d(TAG,"---------------------------");
                        exception = true;
                    } finally {
                        updatePager();
                        rightFragment = rf;
                    }
                }

            } else {
                boolean exception = false;
                panelSelected++;
                if (panelSelected==2 && twoPanel) {
                    mPager.setCurrentItem(panelSelected - 2);
                }
                mAdapter.addFragment(rf);
                mAdapter.notifyDataSetChanged();
                try{
                    mPager.setCurrentItem(panelSelected, true);
                } catch (Exception e) {
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG,"---------------------------");
                    Log.d(TAG,"It as been exception!!!!!!!");
                    Log.d(TAG,"---------------------------");
                    exception = true;
                } finally {
                    if (exception){
                         mPager.setCurrentItem(panelSelected, true);
                    }
                }
               rightFragment = rf;
               leftFragment = mAdapter.mFragments.get(mAdapter.mFragments.size()-2);
            }

        } else {
            if (fragment == leftFragment) {

            } else {
                boolean exception = false;
                panelSelected++;
                if (panelSelected==2 && twoPanel) {
                    mPager.setCurrentItem(panelSelected - 2);
                }
                mAdapter.addFragment(rf);
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

        }

        Log.d(TAG,"----------------------------------------------------------------------");
        Log.d(TAG,"------------------- ADD FRAGMENT--------------------------------------");
        Log.d(TAG,"Number of fragments in mAdapter: " +mAdapter.mFragments.size());
        Log.d(TAG,"Number of fragments in getSupportFragmentManager(): " +getSupportFragmentManager().getFragments().size());
        Log.d(TAG,"----------------------------------------------------------------------");
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
            super.onBackPressed(); // This will pop the Activity from the stack.
        }

        Log.d(TAG,"----------------------------------------------------------------------");
        Log.d(TAG,"------------------- REMOVE FRAGMENT-----------------------------------");
        Log.d(TAG,"Number of fragments in mAdapter: " +mAdapter.mFragments.size());
        Log.d(TAG,"Number of fragments in getSupportFragmentManager(): " +getSupportFragmentManager().getFragments().size());
        Log.d(TAG,"----------------------------------------------------------------------");
    }

    public void updatePager() {
        mPager.setAdapter(null);
        mPager.setAdapter(mAdapter);
        Log.d(TAG,"Panel selected in updatePager: "+panelSelected);
        mPager.setCurrentItem(panelSelected,true);
    }
}
