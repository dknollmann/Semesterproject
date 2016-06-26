package com.semesterdomain.semesterprojekt;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * The type Show pop up.
 */
public class ShowPopUp extends Activity {

    /**
     * The constant WINDOW_WIDTH.
     */
    private static double WINDOW_WIDTH = 0.8;
    /**
     * The constant WINDOW_HEIGHT.
     */
    private static double WINDOW_HEIGHT = 0.2;
    /**
     * The constant WINDOW_DimAmount.
     */
    private static double WINDOW_DimAmount = 0.3;

    /**
     * The Db h.
     */
    SQLiteDBHelper dbH = new SQLiteDBHelper(this);

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
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

    /**
     * Pop up save.
     *
     * @param view the view
     */
    public void popUpSave(View view) {
        Intent intent = getIntent();
        ShoppingList tmp_list = (ShoppingList) intent.getSerializableExtra("shoppingList");
        dbH.addDBList(tmp_list);
        intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
        finish();
    }

    /**
     * Pop up throw.
     *
     * @param view the view
     */
    public void popUpThrow(View view) {
        Intent intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
        finish();
    }

}


