package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import java.util.ArrayList;

/**
 * The type ActivityListEditor is the Activity which handles the editing of a shoppinglist.
 */
public class ActivityListEditor extends AppCompatActivity {

    /**
     * The searchAutoComplete is used to search the products inside the DB and Autocomplete the
     * queried results in a displayed dropdown list.
     */
    SearchAutoCompleteView searchAutoComplete;

    /**
     * The Item.
     */
    ArrayList<Product> item = new ArrayList<Product>();

    /**
     * The adapter for the autoCompleteSearch.
     */
    SearchAutoCompleteAdapter searchAutoCompleteAdapter;
    /**
     * The Adapter.
     */
    ProductListAdapter adapter;

    /**
     * The Product lv.
     */
    ListView product_lv;
    /**
     * The Et list name.
     */
//displays ShoppingListname
    EditText et_list_name;
    /**
     * The Et budget.
     */
//displays budget
    EditText et_budget;

    /**
     * The My shopping list.
     */
    ShoppingList myShoppingList;
    /**
     * The Prod list items.
     */
    ArrayList<Product> prodListItems;
    /**
     * The Db h.
     */
    SQLiteDBHelper dbH = new SQLiteDBHelper(this);

    /**
     * The constant GENERATIONS is the number of Generations for the EA.
     */
    private final int GENERATIONS = 30;

    /**
     * The constant POPULATION_SIZE is the size of the generated Population for the EA.
     */
    private final int POPULATION_SIZE = 30;

    /**
     * The constant NANO_TO_MILLI is used to convert Nanoseconds into Milliseconds.
     */
    private final int NANO_TO_MILLI = 1000000;

    private final int BREAK_EA = 110000;

    /**
     * On restart.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, ActivityHomescreen.class);

        intent.putExtra("shoppingList", myShoppingList);
        //Log.d("LOG", "onRestartActivityList");
        startActivity(intent);
    }

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
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
            dbH.addDBList(myShoppingList);
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
                    dbH.updateDBShoppingListName(myShoppingList);
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

    /**
     * Add product.
     *
     * @param view the view
     */
    public void addProduct(View view) {
        if (searchAutoComplete.product != null) {
            adapter.add(searchAutoComplete.product);
            myShoppingList.getMyProducts().add(searchAutoComplete.product);
            //Log.d("LOG ", "" + myShoppingList.getListId());
            dbH.addDBProductToList(searchAutoComplete.product, myShoppingList);
        }
        displaySumPrice();
    }


    /**
     * Displays the SumPrice inside the view.
     */
    public void displaySumPrice() {
        TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
        sumPrice.setText(myShoppingList.calculateSumPrice(prodListItems) + "€");
    }

    /**
     * On back pressed.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityHomescreen.class);
        startActivity(intent);
    }

    /**
     * Gets items from db.
     *
     * @param searchTerm the search term
     * @return the items from db
     */
// this function is used in CustomAutoCompleteTextChangedListener.java
    public ArrayList<Product> getItemsFromDb(String searchTerm) {

        // add items on the array dynamically
        ArrayList<Product> products = dbH.getDBProductsBySearchTerm(searchTerm);
        return products;
    }

    /**
     * Print array list.
     *
     * @param list the list
     */
    public void printArrayList(ArrayList<Product> list) {
        for (Product prod : list) {
            //Log.d("LOG", prod.getProductName());
        }
        //Log.d("LOG", "printArrayList");
    }

    /**
     * Sort by ga.
     *
     * @param view the view
     */
    public void sortByGA(View view) {
        //long startTime = System.nanoTime();
        //long endTime = System.nanoTime();
        //long executionTime = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        //Log.d("DB_LOG", "executionTime for onResume (ActuvutyHomescreen: " + String.valueOf(executionTime));

        long startTime = System.nanoTime();

        EATourManager.destinationProducts = myShoppingList.getMyProducts();
        printArrayList(myShoppingList.getMyProducts());
        EAPopulation pop = new EAPopulation(POPULATION_SIZE);
        //Log.d("LOG", "EAPopulation erstellt");

        pop = EATravlingSalesman.selection(pop);
        //Log.d("LOG", "population selection");
        EATour bestEATour = pop.getFittest();
        for (int i = 0; i < GENERATIONS; i++) {
            long checkTime = System.nanoTime();
            if(((checkTime - startTime)/NANO_TO_MILLI) < BREAK_EA){
                break; //break if the 2min timewindow is to short.
            }

            pop = EATravlingSalesman.selection(pop);
            if (bestEATour.getDistance() > pop.getFittest().getDistance()) {
                //Log.d("LOG", "best tour distance: " + bestEATour.getDistance());
                double test = bestEATour.getDistance();
                bestEATour = pop.getFittest();
            }
        }
        Log.d("EA_LOG", "executionTime for sortByGA" + String.valueOf((System.nanoTime() - startTime) / NANO_TO_MILLI));
        ArrayList<Product> tempTour = bestEATour.getTour();

        //Remove entrance and cash register
        tempTour.remove(0);
        tempTour.remove(tempTour.size() - 1);

        myShoppingList.setMyProducts(tempTour);

        //Update position in Database
        dbH.updateDBProductPositionsInList(myShoppingList);
        adapter.clear();
        adapter.addAll(myShoppingList.getMyProducts());
        printArrayList(EATourManager.destinationProducts);
        printArrayList(myShoppingList.getMyProducts());

    }

    /**
     * Delete text.
     *
     * @param view the view
     */
    public void deleteText(View view) {
        searchAutoComplete.setText("");
        searchAutoComplete.product = null;
    }
}
