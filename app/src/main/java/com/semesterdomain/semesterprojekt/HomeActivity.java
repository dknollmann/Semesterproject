package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Shopping_List> mainList = new ArrayList<>();
    ListView view_mainList;
    ShoppingListAdapter sadapter;
    //ShoppingDB db;
    String dbPath = "/bla/main/assets/TestDB.sqlite";
    File file = new File("/src/main/assets/TestDB.sqlite" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view_mainList = (ListView) findViewById(R.id.mainList);

        sadapter = new ShoppingListAdapter(this, mainList);

        view_mainList.setAdapter(sadapter);

        //db = new ShoppingDB(getBaseContext());

    }

    public void addList(View view) {
        Intent intent = new Intent(this, ActivityList.class);
        startActivity(intent);
    }


    public void testklick(View view) {
        //SQLiteDatabase sqlDB = db.getReadableDatabase();
        SQLiteDatabase sqlDB = SQLiteDatabase.openOrCreateDatabase(file, null);
        String[] args = {};

        Cursor c = sqlDB.rawQuery("SELECT * FROM TEST", args );
        c.moveToFirst();
        String test = c.getString(c.getColumnIndexOrThrow("meintest"));
        sqlDB.close();


        //Intent intent = getIntent();

        //Shopping_List shoppingList = (Shopping_List) intent.getSerializableExtra("shoppingList");
        Button btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setText(test);

        //sadapter.add(shoppingList);
        //sadapter.notifyDataSetChanged();*/
    }
}
