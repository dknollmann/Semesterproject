package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Shopping_List> mainList = new ArrayList<>();
    ListView view_mainList;
    ShoppingListAdapter sadapter;
    private ShoppingDBHelper db;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view_mainList = (ListView) findViewById(R.id.mainList);

        sadapter = new ShoppingListAdapter(this, mainList);

        view_mainList.setAdapter(sadapter);

        db = new ShoppingDBHelper(this.getApplicationContext());

        db.create_database();


    }


    public void addList(View view) {
        Intent intent = new Intent(this, ActivityList.class);


        startActivity(intent);
    }

    public void testklick(View view) {

        Intent intent = getIntent();
        Log.d("LOG","before passing Shopping List");
        Shopping_List shoppingList = (Shopping_List) intent.getSerializableExtra("shoppingList");
        Log.d("shoppingList", shoppingList.getName());
        Log.d("LOG","after passing Shopping List");
        //sadapter = new ShoppingListAdapter(this, mainList);
        //view_mainList.setAdapter(sadapter);

        sadapter.add(shoppingList);
        db.insertList(shoppingList);
       // sadapter.notifyDataSetChanged();*/
       /* Product p = db.get_Product("Testproduct");
        Button btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setText(p.getManufacturer());
        */
    }
}
