package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class SwiperActivityHomescreen extends Swiper {

    ArrayList<ShoppingList> mainList;
    User user;

    public SwiperActivityHomescreen(ListView view_mainList, Context context, User user, AppCompatActivity activity, ArrayList<ShoppingList> homescreenList) {
        super(view_mainList, context, activity);
        this.mainList = homescreenList;
        this.user = user;
    }

    @Override
    protected void onItemSwipeLeft(final View v, float x) {

        v.setEnabled(false); //need to disable the view for the animation to run

        // stacked the animations to have the pause before the views flings off screen
        v.animate().setDuration(ANIMATION_DURATION).translationX(-v.getWidth() / 3).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().setDuration(ANIMATION_DURATION).alpha(0).translationX(-v.getWidth()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mSwiping = false;
                        mItemPressed = false;
                        animateRemoval(listView, v);
                    }
                });
            }
        });
        mDownX = x;
        swiped = true;
        int i = listView.getPositionForView(v);
        dbH.deleteDBList(user, mainList.get(i));

    }

    @Override
    protected void onItemTouch(View v) {

        listView.setEnabled(true);

        int i = listView.getPositionForView(v);
        Intent intent = new Intent(context, ActivityListEditor.class);
        ShoppingList shoppingList = (ShoppingList) listView.getItemAtPosition(i);
        intent.putExtra("shoppingListForward", shoppingList);
        activity.startActivity(intent);
    }
}
