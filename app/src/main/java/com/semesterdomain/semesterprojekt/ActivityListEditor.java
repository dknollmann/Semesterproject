package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityListEditor extends AppCompatActivity {

    //SearchView prodSearchView;
    SearchAutoCompleteView searchAutoComplete;

    ArrayList<Product> item = new ArrayList<Product>();

    // adapter for auto-complete
    SearchAutoCompleteAdapter searchAutoCompleteAdapter;
    ProductListAdapter adapter;

    ListView product_lv;
    //displays ShoppingListname
    EditText et_list_name;
    //displays budget
    EditText et_budget;

    ShoppingList myShoppingList;
    ArrayList<Product> prodListItems;
    SQLiteDBHelper dbH = new SQLiteDBHelper(this);

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, ActivityHomescreen.class);

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
        et_list_name = (EditText) findViewById(R.id.et_shoppingListname);
        et_budget = (EditText) findViewById(R.id.et_budget);

        searchAutoComplete = (SearchAutoCompleteView) findViewById(R.id.myautocomplete);

        // add the listener so it will tries to suggest while the user types
        searchAutoComplete.addTextChangedListener(new SearchAutoCompleteTextChangedListener(this));


        //myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
        searchAutoCompleteAdapter = new SearchAutoCompleteAdapter(ActivityListEditor.this, item);
        searchAutoComplete.setAdapter(searchAutoCompleteAdapter);
        searchAutoComplete.setSingleLine();


        prodListItems = new ArrayList<Product>();

        Intent intent = getIntent();
        myShoppingList = (ShoppingList) intent.getSerializableExtra("shoppingListForward");


        if (myShoppingList == null) {
            myShoppingList = new ShoppingList();
            myShoppingList.setName("Meine Einkaufsliste");

            SwiperActivityListEditor swiper = new SwiperActivityListEditor(product_lv, this.getApplicationContext(), this, prodListItems, myShoppingList);
            adapter = new ProductListAdapter(this, prodListItems, swiper);
            product_lv.setAdapter(adapter);

            //neuimplementierung
            dbH.insertList(myShoppingList);
        } else {
            SwiperActivityListEditor swiper = new SwiperActivityListEditor(product_lv, this.getApplicationContext(), this, prodListItems, myShoppingList);
            adapter = new ProductListAdapter(this, prodListItems, swiper);
            product_lv.setAdapter(adapter);

            adapter.clear();
            for (Product p : myShoppingList.getMyProducts()) { //dbH.getDBProductsFromList(myShoppingList)
                adapter.add(p);
                //Log.d("LOG", "add");
            }
            displaySumPrice();
        }

        et_list_name.addTextChangedListener(new AfterTextChangedWatcher(myShoppingList, et_list_name));
        et_list_name.setText(myShoppingList.getName());
        et_list_name.setSingleLine();
        et_list_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    dbH.updateShoppingListName(myShoppingList);
                }
            }
        });

        et_budget.addTextChangedListener(new AfterTextChangedWatcher(myShoppingList, et_budget));
        et_budget.setText(String.valueOf(myShoppingList.getBudget()));
        et_budget.setSingleLine();
        et_budget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    myShoppingList.setBudget(Integer.parseInt(et_budget.getText().toString()));
                    dbH.updateDBBudgetForList(myShoppingList);
                }
            }
        });


        //define Header of Productlist
        /*list_header = new EditText(this);
        list_header.addTextChangedListener(new AfterTextChangedWatcher(myShoppingList, list_header));
        list_header.setText(myShoppingList.getName());
        list_header.setSingleLine();*/
        //   product_lv.addHeaderView(list_header);

        //For the search widget
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        //prodSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //prodSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        //Log.d("LOG", "onCreateListActivity");

    }

    public void addProduct(View view) {
        if (searchAutoComplete.product != null) {
            adapter.add(searchAutoComplete.product);
            myShoppingList.getMyProducts().add(searchAutoComplete.product);
            Log.d("LOG ", "" + myShoppingList.getListId());
            dbH.setProductToList(searchAutoComplete.product, myShoppingList);
        }
        displaySumPrice();
    }


    public void displaySumPrice() {
        TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
        sumPrice.setText(myShoppingList.calculateSumPrice(prodListItems) + "€");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
    public ArrayList<Product> getItemsFromDb(String searchTerm) {

        // add items on the array dynamically
        ArrayList<Product> products = dbH.getDBProductsBySearchTerm(searchTerm);
        return products;
    }

    public void printArrayList(ArrayList<Product> list) {
        for (Product prod : list) {
            Log.d("LOG", prod.getProductName());
        }
        Log.d("LOG", "______________");
    }

    public void sortByGA(View view) {
        EATourManager.destinationProducts = myShoppingList.getMyProducts();
        printArrayList(myShoppingList.getMyProducts());
        EAPopulation pop = new EAPopulation(30);
        Log.d("LOG", "EAPopulation erstellt");

        pop = EATravlingSalesman.selection(pop);
        Log.d("LOG", "1");
        EATour bestEATour = pop.getFittest();
        for (int i = 0; i < 30; i++) {
            pop = EATravlingSalesman.selection(pop);
            if (bestEATour.getDistance() > pop.getFittest().getDistance()) {
                Log.d("LOG", "" + bestEATour.getDistance());
                double test = bestEATour.getDistance();
                bestEATour = pop.getFittest();
            }
        }
        ArrayList<Product> tempTour = bestEATour.getTour();

        //Remove entrance and cash register
        tempTour.remove(0);
        tempTour.remove(tempTour.size() - 1);

        myShoppingList.setMyProducts(tempTour);

        //Update position in Database
        dbH.updateProductPositionsInList(myShoppingList);
        adapter.clear();
        adapter.addAll(myShoppingList.getMyProducts());
        printArrayList(EATourManager.destinationProducts);
        printArrayList(myShoppingList.getMyProducts());

    }

    public void deleteText(View view) {
        searchAutoComplete.setText("");
        searchAutoComplete.product = null;
    }
}
