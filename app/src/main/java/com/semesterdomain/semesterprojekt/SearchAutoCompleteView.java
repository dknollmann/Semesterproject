package com.semesterdomain.semesterprojekt;

/**
 * Created by L 875 on 15.06.2016.
 */

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class SearchAutoCompleteView extends AutoCompleteTextView{

    ShoppingDBHelper dbH = new ShoppingDBHelper(null);
    Product product;
    Shopping_List shopping_list;

    public SearchAutoCompleteView(Context context) {
        super(context);
        this.product = null;
        this.shopping_list = null;
        // TODO Auto-generated constructor stub
    }

    public SearchAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
           // TODO Auto-generated constructor stub
    }

    public SearchAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }

        // this is how to disable AutoCompleteTextView filter
        @Override
        protected void performFiltering(final CharSequence text, final int keyCode) {
            String filterText = "";
            super.performFiltering(filterText, keyCode);
        }

        /*
         * after a selection we have to capture the new value and append to the existing text
         */
        @Override
        protected void replaceText(final CharSequence text) {
            super.replaceText(text);

            product = dbH.get_ProductFromDBbyID(""+text);

            setText(product.getProductname());
        }
}
