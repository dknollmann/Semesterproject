package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by L 875 on 16.06.2016.
 */
public abstract class Swipe implements View.OnTouchListener{

    float mDownX;
    private int mSwipeSlop = -1;
    boolean swiped;

    ListView view_mainList;
    Context context;
    ShoppingDBHelper dbH;
    User user;
    AppCompatActivity activity;
    ArrayList<Shopping_List> mainList;

    public Swipe(){

    }

    public Swipe(ListView view_mainList, Context context, User user, AppCompatActivity activity, ArrayList<Shopping_List> mainList){
        this.view_mainList = view_mainList;
        this.context = context;
        this.dbH = new ShoppingDBHelper(context);
        this.user = user;
        this.activity = activity;
        this.mainList = mainList;
        Log.d("LOG","Constructor Swipe");
    }

    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view
    private static final int SWIPE_DURATION = 250; // needed for velocity implementation
    private static final int MOVE_DURATION = 150;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    // animates the removal of the view, also animates the rest of the view into position
    private void animateRemoval(final ListView listView, View viewToRemove)
    {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        final ArrayAdapter adapter = (ArrayAdapter)view_mainList.getAdapter();
        for (int i = 0; i < listView.getChildCount(); ++i)
        {
            View child = listView.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = listView.getAdapter().getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }

        adapter.remove(adapter.getItem(listView.getPositionForView(viewToRemove)));

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i) {
                    final View child = listView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mSwiping = false;
                                        view_mainList.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run()
                                {
                                    mSwiping = false;
                                    view_mainList.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (mSwipeSlop < 0)
        {
            mSwipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mItemPressed)
                {
                    // Doesn't allow swiping two items at same time
                    return false;
                }
                mItemPressed = true;
                mDownX = event.getX();
                swiped = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setTranslationX(0);
                mItemPressed = false;
                break;
            case MotionEvent.ACTION_MOVE:
            {
                float x = event.getX() + v.getTranslationX();
                float deltaX = x - mDownX;
                float deltaXAbs = Math.abs(deltaX);

                if (!mSwiping)
                {
                    if (deltaXAbs > mSwipeSlop) // tells if user is actually swiping or just touching in sloppy manner
                    {
                        mSwiping = true;
                        view_mainList.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (mSwiping && !swiped) // Need to make sure the user is both swiping and has not already completed a swipe action (hence mSwiping and swiped)
                {
                    v.setTranslationX((x - mDownX)); // moves the view as long as the user is swiping and has not already swiped

                    if (deltaX > v.getWidth() / 3) // swipe to right
                    {
                        mDownX = x;
                        swiped = true;
                        mSwiping = false;
                        mItemPressed = false;


                        v.animate().setDuration(300).translationX(v.getWidth()/3); // could pause here if you want, same way as delete
                        TextView tv = (TextView) v.findViewById(R.id.text_shoppingListname);
                        //tv.setText("Swiped!");
                        return true;
                    }
                    else if (deltaX < -1 * (v.getWidth() / 3)) // swipe to left
                    {

                        v.setEnabled(false); // need to disable the view for the animation to run

                        // stacked the animations to have the pause before the views flings off screen
                        v.animate().setDuration(300).translationX(-v.getWidth()/3).withEndAction(new Runnable() {
                            @Override
                            public void run()
                            {
                                v.animate().setDuration(300).alpha(0).translationX(-v.getWidth()).withEndAction(new Runnable()
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
                        return true;
                    }
                }

            }
            break;
            case MotionEvent.ACTION_UP:
            {
                if (mSwiping) // if the user was swiping, don't go to the and just animate the view back into position
                {
                    v.animate().setDuration(300).translationX(0).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mSwiping = false;
                            mItemPressed = false;
                            view_mainList.setEnabled(true);
                        }
                    });
                }
                else // user was not swiping; registers as a click
                {
                    mItemPressed = false;

                    onItemTouch(v);

                    //Toast.makeText(HomeActivity.this, array.get(i).toString(), Toast.LENGTH_LONG).show();

                    return false;
                }
            }
            default:
                return false;
        }
        return true;
    }

    protected abstract void onItemTouch(View v);
    


}
