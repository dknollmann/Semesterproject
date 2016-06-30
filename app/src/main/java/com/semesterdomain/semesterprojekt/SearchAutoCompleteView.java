package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * The type Search auto complete view.
 */
public class SearchAutoCompleteView extends AutoCompleteTextView {

    /**
     * The Db h.
     */
    SQLiteDBHelper dbH = new SQLiteDBHelper(null);
    /**
     * The Product.
     */
    Product product;
    /**
     * The Shopping list.
     */
    ShoppingList shoppingList;


    /**
     * Instantiates a new Search auto complete view.
     *
     * @param context the context
     */
    public SearchAutoCompleteView(Context context) {
        super(context);
        this.product = null;
        this.shoppingList = null;
    }

    /**
     * Instantiates a new Search auto complete view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public SearchAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Search auto complete view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public SearchAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Perform filtering.
     *
     * @param text    the text
     * @param keyCode the key code
     */
//disables AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    /**
     * Replace text.
     *
     * @param text the text
     */
//after a selection the new values need to append to the existing text
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);

        product = dbH.getDBProductById(text.toString());

        setText(product.getProductName());
    }
}
