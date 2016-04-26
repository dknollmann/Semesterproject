package com.semesterdomain.semesterprojekt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<Product> prodListItems=new ArrayList<Product>();

    //DEFINING AN ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ProdListAdapter adapter;
    SearchView prodSearchView;
    ListView product_lv;
    EditText list_header;
    Shopping_List myShoppingList;

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("shoppingList", myShoppingList);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        product_lv = (ListView) findViewById(R.id.list);

        adapter = new ProdListAdapter(this, prodListItems);

        myShoppingList = new Shopping_List();

        product_lv.setAdapter(adapter);

        list_header = new EditText(this);
        list_header.addTextChangedListener(new MyTextWatcher(myShoppingList, list_header));
        list_header.setText("Meine Einkaufsliste");
        list_header.setSingleLine();
        product_lv.addHeaderView(list_header);

        prodSearchView=(SearchView) findViewById(R.id.searchView);

        }

    public void addProduct(View view) {
        if(!prodSearchView.getQuery().toString().isEmpty()) {
            //Die Produkteigenschaften müssen über eine DB Query ermittelt werden
            adapter.add(new Product(prodSearchView.getQuery().toString(), "Hersteller ", 1));
            myShoppingList.setMyProducts(adapter.data);

            TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
            sumPrice.setText(myShoppingList.calcPrice()+ "€");

            adapter.notifyDataSetChanged();
        }
    }

    public void tes(View view) {
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("shoppingList", myShoppingList);

        startActivity(intent);

    }
}
