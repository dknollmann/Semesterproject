package com.semesterdomain.semesterprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by L 875 on 22.04.2016.
 */
public class ProdListAdapter extends BaseAdapter {

        Context context;
        ArrayList<Product> data;
        private static LayoutInflater inflater = null;

        public ProdListAdapter(Context context, ArrayList<Product> data) {
            // TODO Auto-generated constructor stub
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

        public void add(Product s){
            data.add(s);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.prod_list_item, null);

            TextView name = (TextView) vi.findViewById(R.id.text_prodname);
            name.setText(data.get(position).name);

            TextView producer = (TextView) vi.findViewById(R.id.text_producer);
            producer.setText(data.get(position).producer);

            TextView price = (TextView) vi.findViewById(R.id.text_prodprice);
            price.setText(data.get(position).price);

            return vi;
        }

}
