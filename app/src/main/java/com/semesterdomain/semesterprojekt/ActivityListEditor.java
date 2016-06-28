package com.semesterdomain.semesterprojekt;

import android.content.Intent;
import android.graphics.Color;
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
     * This ListView is used for diplaying products.
     */
    ListView product_lv;
    /**
     * This EditText is used for display the ShoppingListname.
     */
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
    private final int GENERATIONS = 1000;

    /**
     * The constant POPULATION_SIZE is the size of the generated Population for the EA.
     */
    private final int POPULATION_SIZE = 10000;

    /**
     * The constant NANO_TO_MILLI is used to convert Nanoseconds into Milliseconds.
     */
    private final int NANO_TO_MILLI = 10000;

    /**
     * The constant BREAK_EA is used to stop the EA when timeWindows is exceeded.
     */
    private final int BREAK_EA = 100;

    /**
     * On restart start the ActivityHomescreen with the shoppinglist as an extra.
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
     * On create implements the core functionality for this Acitivity.
     *
     * @param savedInstanceState the savedInstanceState for properly displaying the Acitivity.
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

            dbH.addDBList(myShoppingList);
        } else {
            SwiperActivityListEditor swiper = new SwiperActivityListEditor(product_lv, this.getApplicationContext(), this, prodListItems, myShoppingList);
            adapter = new ProductListAdapter(this, prodListItems, swiper);
            product_lv.setAdapter(adapter);

            adapter.clear();
            for (Product p : myShoppingList.getMyProducts()) {
                adapter.add(p);
                //Log.d("LOG", "add");
            }
            displaySumPrice(myShoppingList);
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
                    EditText budget = (EditText) findViewById(R.id.et_budget);
                    TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
                    if(myShoppingList.getBudget() < myShoppingList.getSumPrice()){
                        budget.setTextColor(Color.RED);
                        sumPrice.setTextColor(Color.RED);
                    }else{
                        budget.setTextColor(Color.BLACK);
                        sumPrice.setTextColor(Color.BLACK);
                    }
                }
            }
        });
    }

    /**
     * Is used for the add product button.
     *
     * @param view the view
     */
    public void addProduct(View view) {
        if (searchAutoComplete.product != null) {
            adapter.add(searchAutoComplete.product);
            myShoppingList.getMyProducts().add(searchAutoComplete.product);
            dbH.addDBProductToList(searchAutoComplete.product, myShoppingList);
        }
        displaySumPrice(myShoppingList);
        if(!checkPrice(myShoppingList.getBudget(), myShoppingList.calculateSumPrice())){
            EditText budget = (EditText) findViewById(R.id.et_budget);
            TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
            budget.setTextColor(Color.RED);
            sumPrice.setTextColor(Color.RED);
            Log.d("LOGSUM", myShoppingList.getBudget()+"");
            Log.d("LOGSUM", myShoppingList.calculateSumPrice()+"");
        }
    }


    /**
     * Displays the SumPrice inside the view.
     */
    public void displaySumPrice(ShoppingList list) {
        TextView sumPrice = (TextView) findViewById(R.id.text_sumPrice);
        sumPrice.setText(list.calculateSumPrice()+"");
    }

    /**
     * When the backbutton on the buttom side of the smartphone is pressed the ActivityHomescreen is started.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityHomescreen.class);
        if(et_budget.hasFocus()) {
            et_budget.clearFocus();
        }
        if (et_list_name.hasFocus()){
            et_list_name.clearFocus();
        }
        startActivity(intent);
    }

    /**
     * Gets products from the DB based on the typed searchTerm.
     *
     * @param searchTerm the searchTerm is used to search the DB.
     * @return the items from db
     */
// this function is used in CustomAutoCompleteTextChangedListener.java
    public ArrayList<Product> getItemsFromDb(String searchTerm) {

        // add items to the array dynamically
        ArrayList<Product> products = dbH.getDBProductsBySearchTerm(searchTerm);
        return products;
    }

    /**
     * Prints the name of the products of an ArrayList to Log.d.
     *
     * @param list the list
     */
    public void printArrayList(ArrayList<Product> list) {
        for (Product prod : list) {
            //Log.d("LOG", prod.getProductName());
        }
    }

    /**
     * Used for the sort button. This Method launches the EA impelmention and handles the iterations of the generations.
     *
     * @param view the view that is used to display the sorted shoppinglist.
     */
    public void sortByGA(View view) {
        //long startTime = System.nanoTime();
        //long endTime = System.nanoTime();
        //long executionTime = (endTime - startTime) / NANO_TO_MILLI;
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
            if(((checkTime - startTime)/NANO_TO_MILLI) > BREAK_EA){
                //Log.d("EA_LOG", "executionTime for sortByGA: checkTime failed.");
                break; //break if the 2min timewindow is to short.
            }

            pop = EATravlingSalesman.selection(pop);
            if (bestEATour.getDistance() > pop.getFittest().getDistance()) {
                //Log.d("LOG", "best tour distance: " + bestEATour.getDistance());
                double test = bestEATour.getDistance();
                bestEATour = pop.getFittest();
            }
        }
        long endTime = System.nanoTime();
        //Log.d("EA_LOG", "executionTime for sortByGA: " + String.valueOf((endTime - startTime) / NANO_TO_MILLI));
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
     * Used for the delete button next to the search input it clears the typed text.
     *
     * @param view the view that is used to display the delete button.
     */
    public void deleteText(View view) {
        searchAutoComplete.setText("");
        searchAutoComplete.product = null;
    }
    public boolean checkPrice(int budget, int totalCost) {
        if (budget < totalCost) {
            return false;
        }
        return true;
    }
}

