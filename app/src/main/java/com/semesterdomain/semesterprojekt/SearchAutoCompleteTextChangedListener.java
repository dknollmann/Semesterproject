package com.semesterdomain.semesterprojekt;

/**
 * Created by L 875 on 15.06.2016.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class SearchAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    SQLiteDBHelper dbH;

    public SearchAutoCompleteTextChangedListener(Context context) {
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // if you want to see in the logcat what the user types
        Log.d("User input: ", "" + userInput);

        ActivityList activityList = ((ActivityList) context);

        // query the database based on the user input
        activityList.item = activityList.getItemsFromDb(userInput.toString());

        // update the adapater
        activityList.searchAutoCompleteAdapter.notifyDataSetChanged();
        activityList.searchAutoCompleteAdapter = new SearchAutoCompleteAdapter(activityList, activityList.item);
        activityList.searchAutoComplete.setAdapter(activityList.searchAutoCompleteAdapter);

    }

}
