package com.semesterdomain.semesterprojekt;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class ShowPopUp extends Activity {

    private static double WINDOW_WIDTH = 0.8;
    private static double WINDOW_HEIGHT = 0.2;
    private static double WINDOW_DimAmount = 0.3;

    SQLiteDBHelper dbH = new SQLiteDBHelper(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_show_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * WINDOW_WIDTH), (int) (height * WINDOW_HEIGHT));
        getWindow().setDimAmount((float) WINDOW_DimAmount);
    }

    public void popUpSave(View view) {
        Intent intent = getIntent();
        ShoppingList tmp_list = (ShoppingList) intent.getSerializableExtra("shoppingList");
        dbH.insertList(tmp_list);
        intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
        finish();
    }

    public void popUpThrow(View view) {
        Intent intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
        finish();
    }

}


