package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Activity homescreen.
 */
public class ActivityHomescreen extends AppCompatActivity {

    /**
     * The Main list.
     */
    ArrayList<ShoppingList> mainList = new ArrayList<>();
    /**
     * The View main list.
     */
    ListView viewMainList;
    /**
     * The Shopping list adapter.
     */
    ShoppingListAdapter shoppingListAdapter;
    /**
     * The Db helper.
     */
    private SQLiteDBHelper dbHelper;
    /**
     * The User.
     */
    User user;

    /**
     * The Array list.
     */
//private ListView listView;
    private ArrayList arrayList;

    /**
     * The M swiping.
     */
//Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    /**
     * The M item pressed.
     */
    private boolean mItemPressed = false; // Detects if user is currently holding down a view
    /**
     * The constant SWIPE_DURATION.
     */
    private static final int SWIPE_DURATION = 250; // needed for velocity implementation
    /**
     * The constant MOVE_DURATION.
     */
    private static final int MOVE_DURATION = 150;
    /**
     * The M item id top map.
     */
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = new User("Testuser");

        viewMainList = (ListView) findViewById(R.id.mainList);
        SwiperActivityHomescreen swipe = new SwiperActivityHomescreen(viewMainList, this.getApplicationContext(), user, this, mainList);
        shoppingListAdapter = new ShoppingListAdapter(ActivityHomescreen.this, mainList, swipe);
        viewMainList.setAdapter(shoppingListAdapter);

        dbHelper = new SQLiteDBHelper(this.getApplicationContext());

        dbHelper.importDatabase();
    }

    /**
     * On resume.
     */
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ShoppingList> shoppingListsForUser = dbHelper.getShoppingListsByUser(user);
        for (ShoppingList list : shoppingListsForUser) {
            list.setMyProducts(dbHelper.getDBProductsFromList(list));
        }
        shoppingListAdapter.clear();
        shoppingListAdapter.addAll(shoppingListsForUser);

        //Log.d("LOG", "onResume");
    }

    /**
     * Add shopping list.
     *
     * @param view the view
     */
    public void addShoppingList(View view) {
        Intent intent = new Intent(this, ActivityListEditor.class);
        startActivity(intent);
    }
}
