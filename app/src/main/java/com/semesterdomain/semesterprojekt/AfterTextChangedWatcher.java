package com.semesterdomain.semesterprojekt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * The type After text changed watcher.
 */
public class AfterTextChangedWatcher implements TextWatcher {

    /**
     * The Shopping list.
     */
    ShoppingList shoppingList;
    /**
     * The Edit text.
     */
    EditText editText;

    /**
     * Instantiates a new After text changed watcher.
     *
     * @param shoppingList the shopping list
     * @param editText     the edit text
     */
    public AfterTextChangedWatcher(ShoppingList shoppingList, EditText editText) {
        this.shoppingList = shoppingList;
        this.editText = editText;
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
     * @param s      the s
     * @param start  the start
     * @param before the before
     * @param count  the count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * After text changed.
     *
     * @param s the s
     */
    @Override
    public void afterTextChanged(Editable s) {

        shoppingList.setName(editText.getText().toString());
    }
}
