package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<String> {


    View.OnTouchListener mTouchListener;

    public ShoppingListAdapter(Context context, ArrayList<String> values, View.OnTouchListener listener)
    {
        super(context, R.layout.list_item, values);
        mTouchListener = listener;
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Shopping_List slist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);
        }

        View v = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);

        // Lookup view for data population
        TextView tvListName = (TextView) v.findViewById(R.id.text_shoppingListname);
        TextView tvListPrice = (TextView) v.findViewById(R.id.text_shoppingListPrice);

        // Populate the data into the template view using the data object
        tvListName.setText(slist.getName());
        Log.d("LOG", slist.calcPrice(slist.getMyProducts()) +"");
        tvListPrice.setText(slist.calcPrice(slist.getMyProducts())+"");

        v.setOnTouchListener(mTouchListener);

        // Return the completed view to render on screen
        return v;
    }*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View v = inflater.inflate(R.layout.list_item, parent, false);

        TextView b = (TextView) v.findViewById(R.id.list_tv);
        b.setText(getItem(position));

        v.setOnTouchListener(mTouchListener);

        return v;
    }
}
