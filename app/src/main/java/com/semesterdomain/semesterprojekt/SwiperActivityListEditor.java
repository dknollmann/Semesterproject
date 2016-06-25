package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class SwiperActivityListEditor extends Swiper {

    ArrayList<Product> mainList;
    ShoppingList list;

    public SwiperActivityListEditor(ListView view_mainList, Context context, AppCompatActivity activity, ArrayList<Product> listEditorList, ShoppingList shoppingList) {
        super(view_mainList, context, activity);
        this.mainList = listEditorList;
        this.list = shoppingList;
    }

    @Override
    protected void onItemSwipeLeft(final View v, float x) {
        v.setEnabled(false); // need to disable the view for the animation to run

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
        dbH.deleteDBProductFromList(mainList.get(i - 1), list);
    }

    @Override
    protected void onItemTouch(View v) {

    }
}
