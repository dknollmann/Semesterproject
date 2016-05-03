package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<Shopping_List> {

    public ShoppingListAdapter(Context context, ArrayList<Shopping_List> lists) {
        super(context, 0, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Shopping_List slist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvListName = (TextView) convertView.findViewById(R.id.text_shoppingListname);
        TextView tvListPrice = (TextView) convertView.findViewById(R.id.text_shoppingListPrice);

        // Populate the data into the template view using the data object
        tvListName.setText(slist.getName());
        tvListPrice.setText(slist.getSumPrice()+""+ R.string.Euro);

        // Return the completed view to render on screen
        return convertView;
    }
}
