package com.semesterdomain.semesterprojekt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class AfterTextChangedWatcher implements TextWatcher {

    ShoppingList shoppingList;
    EditText editText;

    public AfterTextChangedWatcher(ShoppingList shoppingList, EditText editText) {
        this.shoppingList = shoppingList;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        shoppingList.setName(editText.getText().toString());
    }
}
