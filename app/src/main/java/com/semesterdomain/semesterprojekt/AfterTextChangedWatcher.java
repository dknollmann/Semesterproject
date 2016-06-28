package com.semesterdomain.semesterprojekt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * The type AfterTextChangedWatcher is used to an EditText object so the App can react if user changes a value of an EditText.
 */
public class AfterTextChangedWatcher implements TextWatcher {

    /**
     * The shoppingList where the name should be changed.
     */
    ShoppingList shoppingList;
    /**
     * The EditText where the text should be watched.
     */
    EditText editText;

    /**
     * Instantiates a new AfterTextChangedWatcher.
     *
     * @param shoppingList the shopping list
     * @param editText     the edit text
     */
    public AfterTextChangedWatcher(ShoppingList shoppingList, EditText editText) {
        this.shoppingList = shoppingList;
        this.editText = editText;
    }

    /**
     * Not implemented.
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
     * Not implemented.
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
     * This method will set the a new name for the shoppinglist when the user exits the EditText for the shoppingListName.
     *
     * @param s is the
     */
    @Override
    public void afterTextChanged(Editable s) {

        shoppingList.setName(editText.getText().toString());
    }
}

