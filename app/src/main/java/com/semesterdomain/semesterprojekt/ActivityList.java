package com.semesterdomain.semesterprojekt;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityList extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS


    //DEFINING AN ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ProdListAdapter adapter;
    SearchView prodSearchView;
    SearchAutoCompleteView searchAutoComplete;
    //String[] item = new String[]{"Search here..."};
    List<Product> item = new ArrayList<Product>();
    // adapter for auto-complete
    SearchAutoCompleteAdapter searchAutoCompleteAdapter;
    //ArrayAdapter<String> myAdapter;

    ListView product_lv;
    EditText list_header;
    Shopping_List myShoppingList;
    ArrayList<Product> prodListItems;
    ShoppingDBHelper dbH = new ShoppingDBHelper(this);
    boolean isSaved = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("shoppingList", myShoppingList);
        Log.d("LOG", "onRestartActivityList");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //define views
        product_lv = (ListView) findViewById(R.id.list);
        prodSearchView=(SearchView) findViewById(R.id.searchView);

        searchAutoComplete = (SearchAutoCompleteView) findViewById(R.id.myautocomplete);

        // add the listener so it will tries to suggest while the user types
        searchAutoComplete.addTextChangedListener(new SearchAutoCompleteTextChangedListener(this));

        //myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
        searchAutoCompleteAdapter = new SearchAutoCompleteAdapter(ActivityList.this, item);
        searchAutoComplete.setAdapter(searchAutoCompleteAdapter);

        prodListItems=new ArrayList<Product>();
        adapter = new ProdListAdapter(this, prodListItems);

        Intent intent = getIntent();
        myShoppingList = (Shopping_List) intent.getSerializableExtra("shoppingListForward");

        product_lv.setAdapter(adapter);

        if(myShoppingList == null) {
            myShoppingList = new Shopping_List();
            myShoppingList.setName("Meine Einkaufsliste");
        }else{
            //dbH = new ShoppingDBHelper(this);
            adapter.clear();
            for(Product p : myShoppingList.getMyProducts()){ //dbH.getAllProductsOfList(myShoppingList)
                adapter.add(p);
                Log.d("LOG","add");
            }
            displaySumPrice();
        }

        //define Header of Productlist
        list_header = new EditText(this);
        list_header.addTextChangedListener(new MyTextWatcher(myShoppingList, list_header));
        list_header.setText(myShoppingList.getName());
        list_header.setSingleLine();
        product_lv.addHeaderView(list_header);

        //For the search widget
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        prodSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        prodSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        Log.d("LOG", "onCreateListActivity");

    }

    public void addProduct(View view) {
        //if(!prodSearchView.getQuery().toString().isEmpty()) {
            //Die Produkteigenschaften müssen über eine DB Query ermittelt werden
            //Product prod = dbH.get_ProductFromDB(prodSearchView.getQuery().toString(), "Testmanufacturer");
            adapter.add(searchAutoComplete.product);
            myShoppingList.getMyProducts().add(searchAutoComplete.product);
            displaySumPrice();
            isSaved = false;
        //}
    }


    public void saveByClick(View view) {
        dbH.insertList(myShoppingList);
        isSaved = true;
    }


    public void displaySumPrice(){
        TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
        sumPrice.setText(myShoppingList.calcPrice(prodListItems) + "€");
    }

    @Override
    public void onBackPressed(){
        LayoutInflater inflater = getLayoutInflater();
        /*Log.d("LOG",""+isSaved);
        if(isSaved==false) {

            Intent intent = new Intent(this, ShowPopUp.class);
            intent.putExtra("shoppingList", myShoppingList);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();*/
        if(isSaved==false) {
            new AlertDialog.Builder(this)
                    .setView(inflater.inflate(R.layout.shopping_list_item,null))
                    //.setMessage("Wollen Sie die Liste speichern?")
                    .setPositiveButton("bla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbH.insertList(myShoppingList);
                            ActivityList.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Abbruch", null)
                    .show();
           /* .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbH.insertList(myShoppingList);
                    ActivityList.super.onBackPressed();
                        }
                    }).create().show(); */
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
   public List<Product> getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        List<Product> products = dbH.getAllProductsBySearch(searchTerm);
    /*    int rowCount = products.size();



        String[] item = new String[rowCount];
        int x = 0;

        for (Product record : products) {

            item[x] = record.getProductname();
            Log.d("LOGgetItemsFromDb", item[x]);
            x++;
        }
     */
        return products;
    }


}
