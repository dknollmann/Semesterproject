package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * The type Swiper activity list editor.
 */
public class SwiperActivityListEditor extends Swiper {

    /**
     * The Main list.
     */
    ArrayList<Product> mainList;
    /**
     * The List.
     */
    ShoppingList list;

    /**
     * Instantiates a new Swiper activity list editor.
     *
     * @param view_mainList  the view main list
     * @param context        the context
     * @param activity       the activity
     * @param listEditorList the list editor list
     * @param shoppingList   the shopping list
     */
    public SwiperActivityListEditor(ListView view_mainList, Context context, AppCompatActivity activity, ArrayList<Product> listEditorList, ShoppingList shoppingList) {
        super(view_mainList, context, activity);
        this.mainList = listEditorList;
        this.list = shoppingList;
    }

    /**
     * On item swipe left.
     *
     * @param v the v
     * @param x the x
     */
    @Override
    protected void onItemSwipeLeft(final View v, float x) {
        v.setEnabled(false); // need to disable the view for the animation to run
        Product prodToRemove;

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
        TextView result = (TextView) activity.findViewById(R.id.text_sumPrice);
        prodToRemove = mainList.get(i);

        dbH.deleteDBProductFromList(prodToRemove, list);
        result.setText("" + list.calculateSumPrice());

        if (list.getBudget() >= list.calculateSumPrice()) {
            EditText budget = (EditText) activity.findViewById(R.id.et_budget);
            TextView sumPrice = (TextView) activity.findViewById(R.id.text_sumPrice);
            budget.setTextColor(Color.BLACK);
            sumPrice.setTextColor(Color.BLACK);
        }
    }

    /**
     * On item swipe left.
     *
     * @param v the v
     * @param x the x
     */
    @Override
    protected void onItemSwipeRight(final View v, float x) {
        Product product;
        mDownX = x;
        swiped = true;
        mSwiping = false;
        mItemPressed = false;

        v.animate().setDuration(ANIMATION_DURATION).translationX(v.getWidth() / 3); //could pause here, same way as delete

        int i = listView.getPositionForView(v);
        product = mainList.get(i);

        int isBought = dbH.isMarked(list, product);

        if (isBought > 0) {
            dbH.unMarkAsInShoppingCart(list, product);
            v.setBackgroundColor(Color.parseColor("#fafafa"));
        } else if (isBought == 0) {
            dbH.markAsInShoppingCart(list, product);
            v.setBackgroundColor(Color.GREEN);
        } else {
            Log.d("LOG", "isbought is not set");
        }

    }

    /**
     * On item touch.
     *
     * @param v the v
     */
    @Override
    protected void onItemTouch(View v) {

    }
}
