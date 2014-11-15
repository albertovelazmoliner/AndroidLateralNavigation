package com.fieldaware.viewpagerfragmentstate;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    MyAdapter mAdapter;
    CustomViewPager mPager;
    int panelSelected = 0;
    boolean twoPanel = false;
    final static String TAG = "Puta mierda";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        mPager = (CustomViewPager)findViewById(R.id.pager);
        mPager.setEnabledSwipe(false);
        twoPanel = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        mAdapter = new MyAdapter(getSupportFragmentManager(), twoPanel);

        if (savedInstanceState != null) {
            panelSelected = savedInstanceState.getInt("panelSelected");
        }

        if (getSupportFragmentManager().getFragments() != null){
            for (int i=0; i<getSupportFragmentManager().getFragments().size();i++) {
                //mAdapter.addFragment(i);
            }
        } else {
            mAdapter.list.add(ArrayListFragment.newInstance(panelSelected));
        }
        mAdapter.notifyDataSetChanged();
        mPager.setAdapter(mAdapter);

    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");
        outState.putInt("panelSelected", panelSelected);

    }

    public void onListItemClick(ListView l, View v, int position, long id) {

        panelSelected++;
       // mAdapter.addFragment(panelSelected);
        mAdapter.list.add(ArrayListFragment.newInstance(panelSelected));
        mPager.addView(n);
        mAdapter.notifyDataSetChanged();
        if ((panelSelected > 1 && twoPanel) || !twoPanel) {
            mPager.setCurrentItem(panelSelected, true);
        }

    }

    @Override
    public void onBackPressed() {
        if(panelSelected>1) {
            mPager.setCurrentItem((twoPanel) ? panelSelected - 2 : panelSelected - 1, true);
            //mAdapter.removeLastFragment();
            mAdapter.list.remove(panelSelected);
            mPager.removeViewAt(panelSelected);
            mAdapter.notifyDataSetChanged();
            //mPager.refreshDrawableState();

            panelSelected--;
        } else if (panelSelected>0) {
            //mPager.removeViewAt(1);

            mPager.setCurrentItem(0);
            //mAdapter.removeLastFragment();
            mAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
            panelSelected--;
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    /*public static class MyAdapter extends FragmentPagerAdapter {

        boolean mTwoPane = false;
        private List<Fragment> mFragments = new ArrayList<Fragment>();

        public MyAdapter(FragmentManager fm, boolean twoPane) {
            super(fm);
            this.mTwoPane = twoPane;
        }

        @Override
        public int getItemPosition(Object object) {
            Log.d("Mierdaca", "Number of fragments: "+getCount());
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
    }*/

    public static class MyAdapter extends FragmentPagerAdapter {

        ArrayList<ArrayListFragment> list = new ArrayList<ArrayListFragment>();
        boolean mTwoPane;

        public MyAdapter(FragmentManager fm, boolean twoPanel) {
            super(fm);
            mTwoPane = twoPanel;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }

        @Override
        public float getPageWidth(int position) {
            if (mTwoPane) {
                return(0.5f);
            } else {
                return(1.0f);
            }
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView)tv).setText("Fragment #" + mNum);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, Cheeses.asList()));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
            ((MainActivity)getActivity()).onListItemClick(l,v,position,id);

        }
    }
}
