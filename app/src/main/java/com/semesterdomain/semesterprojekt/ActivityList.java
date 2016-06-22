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

    SearchView prodSearchView;
    SearchAutoCompleteView searchAutoComplete;

    ArrayList<Product> item = new ArrayList<Product>();

    // adapter for auto-complete
    SearchAutoCompleteAdapter searchAutoCompleteAdapter;
    ProdListAdapter adapter;

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

        Intent intent = getIntent();
        myShoppingList = (Shopping_List) intent.getSerializableExtra("shoppingListForward");


        if(myShoppingList == null) {
            myShoppingList = new Shopping_List();
            myShoppingList.setName("Meine Einkaufsliste");

            SwiperActivityList swiper = new SwiperActivityList(product_lv, this.getApplicationContext(), this, prodListItems, myShoppingList );
            adapter = new ProdListAdapter(this, prodListItems, swiper);
            product_lv.setAdapter(adapter);

            //neuimplementierung
            dbH.insertList(myShoppingList);
        }else{
            SwiperActivityList swiper = new SwiperActivityList(product_lv, this.getApplicationContext(), this, prodListItems, myShoppingList );
            adapter = new ProdListAdapter(this, prodListItems, swiper);
            product_lv.setAdapter(adapter);

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
     //   product_lv.addHeaderView(list_header);

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
     //   Product p = searchAutoComplete.product;
        adapter.add(searchAutoComplete.product);
            myShoppingList.getMyProducts().add(searchAutoComplete.product);
            Log.d("LOG ", ""+myShoppingList.getList_id());
            dbH.setProductToList(searchAutoComplete.product, myShoppingList.getList_id());
            displaySumPrice();
            isSaved = false;
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
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
   public ArrayList<Product> getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        ArrayList<Product> products = dbH.getAllProductsBySearch(searchTerm);
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

    public void printArrayList(ArrayList<Product> list){
        for(Product prod : list ){
            Log.d("LOG", prod.getProductname());
        }
        Log.d("LOG", "______________");
    }

    public void sortByGA(View view) {
        TourManager.destinationProducts = myShoppingList.getMyProducts();
        printArrayList(myShoppingList.getMyProducts());
        Population pop = new Population(5);
        Log.d("LOG", "Population erstellt");

        pop = TSP_GA.selection(pop);
        Log.d("LOG", "1");
        Tour bestTour = pop.getFittest();
        for (int i = 0; i < 9; i++) {
            pop = TSP_GA.selection(pop);
            if (bestTour.getDistance() > pop.getFittest().getDistance()){
                Log.d("LOG", ""+bestTour.getDistance());
                double test = bestTour.getDistance();
                bestTour = pop.getFittest();
            }
        }
        ArrayList<Product> tempTour = bestTour.getTour();

        //Remove entrance and cash register
        tempTour.remove(0);
        tempTour.remove(tempTour.size()-1);

        myShoppingList.setMyProducts(tempTour);
        adapter.clear();
        adapter.addAll(myShoppingList.getMyProducts());
        printArrayList(TourManager.destinationProducts);
        printArrayList(myShoppingList.getMyProducts());

    }
}
