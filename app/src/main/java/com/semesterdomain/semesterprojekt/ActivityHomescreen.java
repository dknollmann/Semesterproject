package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityHomescreen extends AppCompatActivity {

    ArrayList<ShoppingList> mainList = new ArrayList<>();
    ListView view_mainList;
    ShoppingListAdapter sadapter;
    private ShoppingDBHelper db;
    User user;

    //private ListView view_mainList;
    private ArrayList array;

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

        view_mainList = (ListView) findViewById(R.id.mainList);
        SwiperActivityHomescreen swipe = new SwiperActivityHomescreen(view_mainList, this.getApplicationContext(), user, this, mainList);
        sadapter = new ShoppingListAdapter(ActivityHomescreen.this, mainList, swipe);
        view_mainList.setAdapter(sadapter);

        db = new ShoppingDBHelper(this.getApplicationContext());

        db.create_database();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ShoppingList> myLists = db.getShoppingListsByUser(user);
        for (ShoppingList list : myLists) {
            list.setMyProducts(db.getAllProductsOfList(list));
        }
        sadapter.clear();
        sadapter.addAll(myLists);

        Log.d("LOG", "onResume");
    }

    public void addList(View view) {
        Intent intent = new Intent(this, ActivityList.class);
        startActivity(intent);
    }
}
