package com.fieldaware.viewpagerfragmentstate;

/**
 * Created by alberto on 18/11/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import android.support.v4.app.ListFragment;

public class SimpleListFragment extends ListFragment {
    private static final String KEY_CONTENTS="contents";
    private static final String[] items= { "lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
            "vel", "erat", "placerat", "ante", "porttitor", "sodales",
            "pellentesque", "augue", "purus" };

    public static SimpleListFragment newInstance() {
        return(newInstance(new ArrayList<String>(Arrays.asList(items))));
    }

    public static SimpleListFragment newInstance(int selectedItem) {
        ArrayList<String> middleContents=new ArrayList<String>();

        for (int i=0; i < 20; i++) {
            middleContents.add(items[selectedItem] + " #" + i);
        }
        return (newInstance(middleContents));
    }

    public static SimpleListFragment newInstance(ArrayList<String> contents) {
        SimpleListFragment result=new SimpleListFragment();
        Bundle args=new Bundle();

        args.putStringArrayList(KEY_CONTENTS, contents);
        result.setArguments(args);

        return(result);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setContents(getArguments().getStringArrayList(KEY_CONTENTS));
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ((MainActivity)getActivity()).onListItemClick(this, position);
    }

    void setContents(ArrayList<String> contents) {
        setListAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.simple_list_item_1,
                contents));
    }

    /*
    @Override
    public void onPause(){
        super.onPause();
        Log.d("threePanelTest","The fragment is onPause after get weight 0");
    }

    @Override
    public void onResume(){
        super.onPause();
        Log.d("threePanelTest","The fragment has started in onResume after get weight 1");
    }*/
}