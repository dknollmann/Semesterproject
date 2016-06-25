package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityHomescreen extends AppCompatActivity {

    ArrayList<ShoppingList> mainList = new ArrayList<>();
    ListView viewMainList;
    ShoppingListAdapter shoppingListAdapter;
    private SQLiteDBHelper dbHelper;
    User user;

    //private ListView viewMainList;
    private ArrayList arrayList;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view
    private static final int SWIPE_DURATION = 250; // needed for velocity implementation
    private static final int MOVE_DURATION = 150;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


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

    public void addShoppingList(View view) {
        Intent intent = new Intent(this, ActivityList.class);
        startActivity(intent);
    }
}
