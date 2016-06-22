package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProdListAdapter extends ArrayAdapter<Product> {

    View.OnTouchListener mTouchListener;

    public ProdListAdapter(Context context, ArrayList<Product> products, View.OnTouchListener listener) {
        super(context, R.layout.prod_list_item, products);
        this.mTouchListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Product product = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.prod_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvProdName = (TextView) convertView.findViewById(R.id.text_prodname);
        TextView tvManufacturer = (TextView) convertView.findViewById(R.id.text_producer);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.text_prodprice);

        // Populate the data into the template view using the data object
        tvProdName.setText(product.getProductname());
        tvManufacturer.setText(product.getManufacturer());
        tvPrice.setText(product.getPrice() + "€");

        convertView.setOnTouchListener(mTouchListener);
        // Return the completed view to render on screen
        return convertView;
    }

}
