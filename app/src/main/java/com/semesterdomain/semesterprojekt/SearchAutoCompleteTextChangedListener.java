package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class SearchAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    SQLiteDBHelper dbH;

    public SearchAutoCompleteTextChangedListener(Context context) {
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        //Log.d("User input: ", "" + userInput);

        ActivityListEditor activityListEditor = ((ActivityListEditor) context);

        //query the database based on the user input
        activityListEditor.item = activityListEditor.getItemsFromDb(userInput.toString());

        //update the adapater
        activityListEditor.searchAutoCompleteAdapter.notifyDataSetChanged();
        activityListEditor.searchAutoCompleteAdapter = new SearchAutoCompleteAdapter(activityListEditor, activityListEditor.item);
        activityListEditor.searchAutoComplete.setAdapter(activityListEditor.searchAutoCompleteAdapter);

    }

}
