package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * The type Search auto complete adapter.
 */
public class SearchAutoCompleteAdapter extends ArrayAdapter<Product> {

    /**
     * Instantiates a new Search auto complete adapter.
     *
     * @param context  the context
     * @param products the products
     */
    public SearchAutoCompleteAdapter(Context context, List<Product> products) {
        super(context, 0, products);

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

        //Get the data item for this position
        Product product = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_autocomplete_list_item, parent, false);
        }
        //Lookup view for data population
        TextView tvProdName = (TextView) convertView.findViewById(R.id.search_text_prodname);
        TextView tvManufacturer = (TextView) convertView.findViewById(R.id.search_text_producer);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.search_text_prodprice);

        //Populate the data into the template view using the data object
        tvProdName.setText(product.getProductName());
        tvManufacturer.setText(product.getManufacturer());
        tvPrice.setText(product.getPrice() + "€");

        //Return the completed view to render on screen
        return convertView;
    }
}
