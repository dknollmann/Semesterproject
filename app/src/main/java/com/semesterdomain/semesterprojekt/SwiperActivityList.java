package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by L 875 on 16.06.2016.
 */
public class SwiperActivityList extends Swipe {

    ArrayList<Product> mainList;

    public SwiperActivityList(ListView view_mainList, Context context, User user, AppCompatActivity activity, ArrayList<Product> mainList){
        super(view_mainList, context, user, activity);
        this.mainList = mainList;
    }

    @Override
    protected void onItemSwipeLeft(View v, float x) {
        
    }

    @Override
    protected void onItemTouch(View v) {

    }
}
