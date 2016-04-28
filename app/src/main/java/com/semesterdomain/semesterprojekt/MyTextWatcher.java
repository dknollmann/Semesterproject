package com.semesterdomain.semesterprojekt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {

    Shopping_List shopping_list;
    EditText et;

    public MyTextWatcher(Shopping_List sl, EditText et) {
        this.shopping_list = sl;
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        shopping_list.setName(""+et.getText());
    }
}
