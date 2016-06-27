package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Activity homescreen is used for handling the user interaction on the Homescreen.
 */
public class ActivityHomescreen extends AppCompatActivity {

    /**
     * The Main list holds all shoppinglists for the user of the App.
     */
    ArrayList<ShoppingList> mainList = new ArrayList<>();
    /**
     * The View main list is the view for the list of shoppinglists.
     */
    ListView viewMainList;
    /**
     * The adapter for shoppinglists.
     */
    ShoppingListAdapter shoppingListAdapter;
    /**
     * The dbHelper is used to query the shoppinglists from DB.
     */
    private SQLiteDBHelper dbHelper;
    /**
     * The User that is using the app at the time.
     */
    User user;
    /**
     * Detects if user is swiping on ACTION_UP.
     */
    private boolean mSwiping = false;
    /**
     * Detects if user is currently holding down a view.
     */
    private boolean mItemPressed = false;
    /**
     * The constant SWIPE_DURATION is needed for the velocity implementation.
     */
    private static final int SWIPE_DURATION = 250;
    /**
     * The constant MOVE_DURATION is used for the duration of the move.
     */
    private static final int MOVE_DURATION = 150;

    //HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    /**
     * On create is called everytime the App is opened.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //at the moment this is just a dummy user so that the DB can be tested.
        user = new User("Testuser0");

        viewMainList = (ListView) findViewById(R.id.mainList);
        SwiperActivityHomescreen swipe = new SwiperActivityHomescreen(viewMainList, this.getApplicationContext(), user, this, mainList);
        shoppingListAdapter = new ShoppingListAdapter(ActivityHomescreen.this, mainList, swipe);
        viewMainList.setAdapter(shoppingListAdapter);

        dbHelper = new SQLiteDBHelper(this.getApplicationContext());

        dbHelper.importDatabase();
    }

    /**
     * On resume loads the shoppinglists from the DB.
     */
    @Override
    protected void onResume() {
        //long startTime = System.nanoTime();
        super.onResume();
        ArrayList<ShoppingList> shoppingListsForUser = dbHelper.getDBShoppingListsByUser(user);
        for (ShoppingList list : shoppingListsForUser) {
            list.setMyProducts(dbHelper.getDBProductsFromList(list));
        }
        shoppingListAdapter.clear();
        shoppingListAdapter.addAll(shoppingListsForUser);
        //Log.d("LOG", "onResume");
        //long endTime = System.nanoTime();
        //long executionTime = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        //Log.d("DB_LOG", "executionTime for onResume (ActuvutyHomescreen: " + String.valueOf(executionTime));
    }

    /**
     * Switches the Activity to the ActivityListEditor when the user presses the add
     * shoppinglist button.
     *
     * @param view the view is the current view.
     */
    public void addShoppingList(View view) {
        Intent intent = new Intent(this, ActivityListEditor.class);
        startActivity(intent);
    }
}
