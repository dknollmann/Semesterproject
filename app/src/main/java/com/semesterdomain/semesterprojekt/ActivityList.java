package com.semesterdomain.semesterprojekt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS


    //DEFINING AN ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ProdListAdapter adapter;
    SearchView prodSearchView;
    ListView product_lv;
    EditText list_header;
    Shopping_List myShoppingList;
    ArrayList<Product> prodListItems;

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("shoppingList", myShoppingList);
        Log.d("LOG", "onRestart");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //define views
        product_lv = (ListView) findViewById(R.id.list);
        prodSearchView=(SearchView) findViewById(R.id.searchView);

        prodListItems=new ArrayList<Product>();
        adapter = new ProdListAdapter(this, prodListItems);

        Intent intent = getIntent();
        myShoppingList = (Shopping_List) intent.getSerializableExtra("shoppingListForward");

        if(myShoppingList == null) {
            myShoppingList = new Shopping_List();
            myShoppingList.setName("Meine Einkaufsliste");
        }

        product_lv.setAdapter(adapter);

        //define Header of Productlist
        list_header = new EditText(this);
        list_header.addTextChangedListener(new MyTextWatcher(myShoppingList, list_header));
        list_header.setText(myShoppingList.getName());
        list_header.setSingleLine();
        product_lv.addHeaderView(list_header);

        Log.d("LOG", "onCreate");

    }

    public void addProduct(View view) {
        if(!prodSearchView.getQuery().toString().isEmpty()) {
            //Die Produkteigenschaften müssen über eine DB Query ermittelt werden
            ShoppingDBHelper sDBH;
            Product prod = new Product(prodSearchView.getQuery().toString(), "Testmanufacturer", 1);
            adapter.add(prod);
            //myShoppingList.getMyProducts().add(prod);

            TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
            sumPrice.setText(myShoppingList.calcPrice(prodListItems) + "€");
        }
    }

    public void passShoppingListToHomeActivity(View view) {
        myShoppingList.setMyProducts(prodListItems);
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("shoppingList", myShoppingList);

        startActivity(intent);

    }

    /*public void test(View view) {
        Intent intent = getIntent();
        Log.d("LOG","before passing Shopping ListName");
        Shopping_List shoppingList = (Shopping_List) intent.getSerializableExtra("shoppingList");
        Log.d("shoppingList", shoppingList.getName());
        Log.d("LOG","after passing Shopping List");
        //sadapter = new ShoppingListAdapter(this, mainList);
        //view_mainList.setAdapter(sadapter);
        list_header.setText(shoppingList.getName());
    }*/
}
