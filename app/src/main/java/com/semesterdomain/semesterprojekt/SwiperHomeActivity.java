package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by L 875 on 16.06.2016.
 */
public class SwiperHomeActivity extends Swipe {

    ArrayList<Shopping_List> mainList;
    User user;

    public SwiperHomeActivity(ListView view_mainList, Context context, User user, AppCompatActivity activity, ArrayList<Shopping_List> mainList){
        super(view_mainList, context, activity);
        this.mainList = mainList;
        this.user = user;
    }

    @Override
    protected void onItemSwipeLeft(final View v, float x) {

            v.setEnabled(false); // need to disable the view for the animation to run

            // stacked the animations to have the pause before the views flings off screen
            v.animate().setDuration(ANIMATION_DURATION).translationX(-v.getWidth()/3).withEndAction(new Runnable() {
                @Override
                public void run()
                {
                    v.animate().setDuration(ANIMATION_DURATION).alpha(0).translationX(-v.getWidth()).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mSwiping = false;
                            mItemPressed = false;
                            animateRemoval(view_mainList, v);
                        }
                    });
                }
            });
            mDownX = x;
            swiped = true;
            int i = view_mainList.getPositionForView(v);
            dbH.deleteList(user, mainList.get(i));

    }

    @Override
    protected void onItemTouch(View v) {

        view_mainList.setEnabled(true);

        int i = view_mainList.getPositionForView(v);
        Intent intent = new Intent(context, ActivityList.class);
        Shopping_List list = (Shopping_List) view_mainList.getItemAtPosition(i);
        Log.d("LOG", "ID: " + list.getList_id());
        intent.putExtra("shoppingListForward", list);
        activity.startActivity(intent);
    }
}