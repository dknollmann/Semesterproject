package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * The type Search auto complete text changed listener.
 */
public class SearchAutoCompleteTextChangedListener implements TextWatcher {

    /**
     * The constant TAG.
     */
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    /**
     * The Context.
     */
    Context context;
    /**
     * The Db h.
     */
    SQLiteDBHelper dbH;

    /**
     * Instantiates a new Search auto complete text changed listener.
     *
     * @param context the context
     */
    public SearchAutoCompleteTextChangedListener(Context context) {
        this.context = context;
    }

    /**
     * After text changed.
     *
     * @param s the s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Before text changed.
     *
     * @param s     the s
     * @param start the start
     * @param count the count
     * @param after the after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * On text changed.
     *
     * @param userInput the user input
     * @param start     the start
     * @param before    the before
     * @param count     the count
     */
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
