package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class SearchAutoCompleteView extends AutoCompleteTextView {

    SQLiteDBHelper dbH = new SQLiteDBHelper(null);
    Product product;
    ShoppingList shoppingList;

    public SearchAutoCompleteView(Context context) {
        super(context);
        this.product = null;
        this.shoppingList = null;
    }

    public SearchAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //disables AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    //after a selection the new values need to append to the existing text
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);

        product = dbH.getDBProductById(text.toString());

        setText(product.getProductName());
    }
}
