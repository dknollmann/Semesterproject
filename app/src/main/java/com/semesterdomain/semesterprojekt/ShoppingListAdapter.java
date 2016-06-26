package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The type Shopping list adapter is for handling the shoppinglists added by the user.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    /**
     * The M touch listener.
     */
    View.OnTouchListener mTouchListener;

    /**
     * Instantiates a new Shopping list adapter.
     *
     * @param context  the context
     * @param values   the values
     * @param listener the listener
     */
    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> values, View.OnTouchListener listener) {
        super(context, R.layout.list_item, values);
        mTouchListener = listener;
    }

    /**
     * Gets view.
     *
     * @param position    the position
     * @param convertView the convert view
     * @param parent      the parent
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ShoppingList shoppingList = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);
        }

        View v = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);

        // Lookup view for data population
        TextView tvListName = (TextView) v.findViewById(R.id.text_shoppingListname);
        TextView tvListPrice = (TextView) v.findViewById(R.id.text_shoppingListPrice);

        // Populate the data into the template view using the data object
        tvListName.setText(shoppingList.getName());
        //Log.d("LOG", shoppingList.calculateSumPrice(shoppingList.getMyProducts()) + "");
        tvListPrice.setText(String.valueOf(shoppingList.calculateSumPrice(shoppingList.getMyProducts())));

        v.setOnTouchListener(mTouchListener);

        // Return the completed view to render on screen
        return v;
    }
}
