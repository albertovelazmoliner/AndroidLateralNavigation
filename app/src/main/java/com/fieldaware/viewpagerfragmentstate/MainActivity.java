package com.fieldaware.viewpagerfragmentstate;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
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
    boolean isEnoughBigToShowAlwaysTwoPanel = false;
    boolean isTwoPanel = false;
    String density;
    final static String TAG = "FieldAware";
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
        float w = getResources().getDisplayMetrics().widthPixels;
        float h = getResources().getDisplayMetrics().heightPixels;
        twoPanel = (w > h) ? true : false;
        isEnoughBigToShowAlwaysTwoPanel = isDeviceEnoughBigToShowAlwaysTwoPanel();
        density = getDensityName(this);
        if (!isTwoPanel()) {
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
        if (!isEnoughBigToShowAlwaysTwoPanel && isTwoPanel()) {
            mAdapter.mTwoPane = !mAdapter.mTwoPane;
            twoPanel = !twoPanel;
        }
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
                    } finally {
                        updatePager();
                        rightFragment = rf;
                    }
                } else {
                    panelSelected++;
                    try{
                        mAdapter.addFragment(rf);
                        mAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG,"---------------------------");
                        Log.d(TAG,"It as been exception!!!!!!!");
                        Log.d(TAG,"---------------------------");
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
            boolean exception = false;
            panelSelected++;
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
            Log.d(TAG,"----------------------------------------------------------------------");
            Log.d(TAG,"------------------- REMOVE FRAGMENT-----------------------------------");
            Log.d(TAG,"Number of fragments in mAdapter: " +mAdapter.mFragments.size());
            Log.d(TAG,"Number of fragments in getSupportFragmentManager(): " +getSupportFragmentManager().getFragments().size());
            Log.d(TAG,"----------------------------------------------------------------------");
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
            Log.d(TAG,"----------------------------------------------------------------------");
            Log.d(TAG,"------------------- REMOVE FRAGMENT-----------------------------------");
            Log.d(TAG,"Number of fragments in mAdapter: " +mAdapter.mFragments.size());
            Log.d(TAG,"Number of fragments in getSupportFragmentManager(): " +getSupportFragmentManager().getFragments().size());
            Log.d(TAG,"----------------------------------------------------------------------");
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

    private static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    private double getDiagonalSize(){
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float widthDpi = metrics.xdpi;
        Log.d(TAG,"widthDpi: "+ widthDpi);
        float heightDpi = metrics.ydpi;
        Log.d(TAG,"heightDpi: "+ heightDpi);
        int widthPixels = metrics.widthPixels;
        Log.d(TAG,"widthPixels: "+ widthPixels);
        int heightPixels = metrics.heightPixels;
        Log.d(TAG,"heightPixels: "+ heightPixels);
        float widthInches = widthPixels / widthDpi;
        Log.d(TAG,"widthInches: "+ widthInches);
        float heightInches = heightPixels / heightDpi;
        Log.d(TAG,"heightInches: "+ heightInches);

        //a² + b² = c²
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

    private boolean isTwoPanel(){
        double diagonalInches = getDiagonalSize();
        Log.d(TAG,"Device diagonal inches: "+diagonalInches);
        if (diagonalInches > 6.5) {
            //Device is a 7" tablet bigger than 7"
            return true;
        }
        return false;
    }

}
