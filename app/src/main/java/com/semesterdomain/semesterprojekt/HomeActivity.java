package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Shopping_List> mainList = new ArrayList<>();
    ListView view_mainList;
    ShoppingListAdapter sadapter;
    private ShoppingDBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        User user = new User("Testuser");

        view_mainList = (ListView) findViewById(R.id.mainList);

        sadapter = new ShoppingListAdapter(this, mainList);

        view_mainList.setAdapter(sadapter);

        view_mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, ActivityList.class);
                Shopping_List list = (Shopping_List) parent.getItemAtPosition(position);
                Log.d("LOG", "ID: " + list.getList_id());
                intent.putExtra("shoppingListForward", list);
                startActivity(intent);
            }
        });

        db = new ShoppingDBHelper(this.getApplicationContext());

        db.create_database();

        ArrayList<Shopping_List> myLists = db.getShoppingListsByUser(user);
        for(Shopping_List list: myLists){
            list.setMyProducts(db.getAllProductsOfList(list));
        }
        sadapter.addAll(myLists);

        //is there a new shopping List from ActivityList than add it
        Intent intent = getIntent();
        Shopping_List tmp_list = (Shopping_List) intent.getSerializableExtra("shoppingList");
        if (tmp_list != null) {
            db.insertList(tmp_list);
            sadapter.add(tmp_list);
        }
    }

    public void addList(View view) {
        Intent intent = new Intent(this, ActivityList.class);
        startActivity(intent);
    }

}
