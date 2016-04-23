package com.semesterdomain.semesterprojekt;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Product> prodListItems=new ArrayList<Product>();

    //DEFINING AN ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ProdListAdapter adapter;

    SearchView prodSearchView;

    ListView product_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        product_lv = (ListView) findViewById(R.id.list);
        adapter = new ProdListAdapter(this, prodListItems);

        product_lv.setAdapter(adapter);

        prodSearchView = (SearchView) findViewById(R.id.searchView);

    }

    public void addProduct(View view) {
        if(!prodSearchView.getQuery().toString().isEmpty()) {
            //Die Produkteigenschaften müssen über eine DB Query ermittelt werden
            adapter.add(new Product(prodSearchView.getQuery().toString(), "Hersteller ", "3,99 €"));
        }
    }
}
