package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * The type Swiper is used to implement other Swipers for ListViews it supports swiping left,
 * swiping right and selecting the ListView element.
 */
public abstract class Swiper implements View.OnTouchListener {

    /**
     * The M down x.
     */
    float mDownX;
    /**
     * The M swipe slop.
     */
    int mSwipeSlop = -1;
    /**
     * The Swiped.
     */
    boolean swiped;

    /**
     * The List view.
     */
    ListView listView;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Db h.
     */
    SQLiteDBHelper dbH;
    /**
     * The Activity.
     */
    AppCompatActivity activity;

    /**
     * Instantiates a new Swiper.
     */
    public Swiper() {

    }

    /**
     * Instantiates a new Swiper.
     *
     * @param listView the list view
     * @param context  the context
     * @param activity the activity
     */
    public Swiper(ListView listView, Context context, AppCompatActivity activity) {
        this.listView = listView;
        this.context = context;
        this.dbH = new SQLiteDBHelper(context);
        this.activity = activity;
    }


    /**
     * On item swipe left.
     *
     * @param v the v
     * @param x the x
     */
    protected abstract void onItemSwipeLeft(final View v, float x);

    /**
     * On item touch.
     *
     * @param v the v
     */
    protected abstract void onItemTouch(View v);

    /**
     * The M swiping.
     */
    protected boolean mSwiping = false; //detects if user is swiping on ACTION_UP
    /**
     * The M item pressed.
     */
    protected boolean mItemPressed = false; //Detects if user is currently holding down a view
    /**
     * The constant SWIPE_DURATION.
     */
    protected static final int SWIPE_DURATION = 250; //needed for velocity implementation
    /**
     * The constant MOVE_DURATION.
     */
    protected static final int MOVE_DURATION = 150;
    /**
     * The constant ANIMATION_DURATION.
     */
    protected static final long ANIMATION_DURATION = 300;
    /**
     * The M item id top map.
     */
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    /**
     * Animate removal.
     *
     * @param listView     the list view
     * @param viewToRemove the view to remove
     */
//animates the removal of the view, also animates the rest of the view into position
    protected void animateRemoval(final ListView listView, View viewToRemove) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        final ArrayAdapter adapter = (ArrayAdapter) this.listView.getAdapter();
        for (int i = 0; i < listView.getChildCount(); ++i) {
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
                                        Swiper.this.listView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        //Animate new views along with the others. The catch is that they did not
                        //exist in the start state, so we must calculate their starting position
                        //based on neighboring views.
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mSwiping = false;
                                    Swiper.this.listView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    /**
     * On touch boolean.
     *
     * @param v     the v
     * @param event the event
     * @return the boolean
     */
    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mItemPressed) {
                    //Doesn't allow swiping two items at same time
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
            case MotionEvent.ACTION_MOVE: {
                if (motionEventActionMoveDetected(v, event)) return true;

            }
            break;
            case MotionEvent.ACTION_UP: {
                if (motionEventActionUpDetected(v)) return false;
            }
            default:
                return false;
        }
        return true;
    }

    /**
     * Motion event action move detected boolean.
     *
     * @param v     the v
     * @param event the event
     * @return the boolean
     */
    private boolean motionEventActionMoveDetected(View v, MotionEvent event) {
        float x = event.getX() + v.getTranslationX();
        float deltaX = x - mDownX;
        float deltaXAbs = Math.abs(deltaX);

        if (!mSwiping) {
            if (deltaXAbs > mSwipeSlop) //tells if user is actually swiping or just touching in sloppy manner
            {
                mSwiping = true;
                listView.requestDisallowInterceptTouchEvent(true);
            }
        }
        if (mSwiping && !swiped) //Need to make sure the user is both swiping and has not already completed a swipe action (hence mSwiping and swiped)
        {
            v.setTranslationX((x - mDownX)); //moves the view as long as the user is swiping and has not already swiped

            if (deltaX > v.getWidth() / 3) //swipe to right
            {
                mDownX = x;
                swiped = true;
                mSwiping = false;
                mItemPressed = false;


                v.animate().setDuration(ANIMATION_DURATION).translationX(v.getWidth() / 3); //could pause here, same way as delete
                TextView tv = (TextView) v.findViewById(R.id.text_shoppingListname);
                //tv.setText("Swiped!");
                return true;
            } else if (deltaX < -1 * (v.getWidth() / 3)) //swipe to left
            {
                onItemSwipeLeft(v, x);
                return true;
            }
        }
        return false;
    }

    /**
     * Motion event action up detected boolean.
     *
     * @param v the v
     * @return the boolean
     */
    private boolean motionEventActionUpDetected(View v) {
        if (mSwiping) //if the user was swiping, don't go to the and just animate the view back into position
        {
            v.animate().setDuration(ANIMATION_DURATION).translationX(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mSwiping = false;
                    mItemPressed = false;
                    listView.setEnabled(true);
                }
            });
        } else //user was not swiping; registers as a click
        {
            mItemPressed = false;

            onItemTouch(v);

            //Toast.makeText(ActivityHomescreen.this, array.get(i).toString(), Toast.LENGTH_LONG).show();

            return true;
        }
        return false;
    }
}
