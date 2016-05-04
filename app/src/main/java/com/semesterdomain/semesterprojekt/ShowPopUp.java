package com.semesterdomain.semesterprojekt;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.*;

public class ShowPopUp extends Activity {

    ShoppingDBHelper dbH = new ShoppingDBHelper(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_show_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.2));
        getWindow().setDimAmount((float) 0.3);
    }

    public void popUpSave(View view){
        Intent intent = getIntent();
        Shopping_List tmp_list = (Shopping_List) intent.getSerializableExtra("shoppingList");
        dbH.insertList(tmp_list);
        intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void popUpThrow(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}


