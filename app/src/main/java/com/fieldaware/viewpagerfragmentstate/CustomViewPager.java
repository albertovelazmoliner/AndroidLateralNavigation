package com.fieldaware.viewpagerfragmentstate;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean mIsEnabledSwipe = true;
    final static String TAG = "FieldAware";

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnabledSwipe) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mIsEnabledSwipe) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setEnabledSwipe(boolean enabled) {
        mIsEnabledSwipe = enabled;
    }

    public void updatePager(LateralNavigationAdapter adapter, int panelSelected) {
        setAdapter(null);
        setAdapter(adapter);
        Log.d(TAG, "Panel selected in updatePager: " + panelSelected);
        setCurrentItem(panelSelected,true);
    }

}