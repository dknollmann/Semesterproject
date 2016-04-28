package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter{

        Context context;
        ArrayList<Shopping_List> data;
        private static LayoutInflater inflater = null;

        public ShoppingListAdapter(Context context, ArrayList<Shopping_List> data) {
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public void add(Shopping_List s){
            data.add(s);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null) {
                vi = inflater.inflate(R.layout.shopping_list_item, null);
            }

            TextView name = (TextView) vi.findViewById(R.id.text_shoppingListname);
            name.setText(data.get(position).getName());

            TextView price = (TextView) vi.findViewById(R.id.text_shoppingListPrice);
            String listPrice = String.format("%d",data.get(position).getSumPrice())+ R.string.Euro;
            price.setText(listPrice);

            return vi;
        }
}
