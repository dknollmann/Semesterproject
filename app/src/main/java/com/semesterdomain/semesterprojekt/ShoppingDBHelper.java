package com.semesterdomain.semesterprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ShoppingDBHelper extends SQLiteOpenHelper {

    //DB Data
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "bla.sqlite";
    private static final String DB_PATH = "/data/data/com.semesterdomain.semesterprojekt/databases/";

    //Tables
    private static final String TBL_PRODUCT = "PRODUCT";
    private static final String TBL_LIST = "LIST";
    private static final String TBL_USER = "USER";
    private static final String TBL_PSEUDO = "PSEUDO";
    private static final String TBL_RIGHT = "RIGHT";
    private static final String TBL_LIST_PRODUCT = "LIST_PRODUCT";
    private static final String TBL_LIST_PSEUDO = "LIST_PSEUDO";


    private final Context mcontext;
    private SQLiteDatabase myDatabase;



    ShoppingDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }

    public void create_database(){

        if(check_database()){
            Log.d("LOG","Database already on device");
        }else{
            this.getReadableDatabase();
            copy_database();
        }

    }

    public boolean check_database(){
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }

    public void copy_database(){

        try {
            InputStream is = mcontext.getResources().getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer)) > 0){
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            Log.d("LOG","Copy Failure");
            e.printStackTrace();
        }
        Log.d("LOG","Database copied on device");
    }

    public void open_database(){
        if(!(myDatabase != null && myDatabase.isOpen())) {
            myDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("LOG", DB_NAME + " opened");
        }
    }

    public void close_database(){
        if(myDatabase != null){
            myDatabase.close();
            Log.d("LOG",DB_NAME + " closed");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //must be filled:
        //increase Version with every update
    }
    public ArrayList<Product> getAllProductsOfList(Shopping_List list){
        open_database();

        Product product;
        String[] args = {""+list.getList_id()};

        Cursor c = myDatabase.rawQuery("SELECT product_id, productname, manufacturer, productprice, posx, posy, listposition FROM LIST_PRODUCT " +
                "JOIN PRODUCT ON LIST_PRODUCT.fk_product = PRODUCT.product_id " +
                "WHERE LIST_PRODUCT.fk_list = ?" +
                "ORDER BY listposition ASC", args);
        list.getMyProducts().clear();
        if(c != null){
            /*c.getCount();
            ArrayList<Product> al = new ArrayList<>();
            */
            if(c.moveToFirst()){
                do{
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProduct_id(c.getInt(0));
                    product.setProductname(c.getString(1));
                    product.setManufacturer(c.getString(2));
                    product.setPrice(c.getInt(3));
                    product.setPosX(c.getInt(4));
                    product.setPosY(c.getInt(5));
                    Log.d("LOGBla", ""+c.getInt(6));
                    list.getMyProducts().add(product);



                }while(c.moveToNext());
            }
        }
        c.close();
        close_database();
        return list.getMyProducts();
    }
    //get Single Productdata
    public Product get_ProductFromDB(String productname, String manufacturer){
        open_database();

        Product product = null;

        String where_clause = "productname = ? AND manufacturer = ?";
        String[] args = {productname, manufacturer};

        Cursor c = myDatabase.query(TBL_PRODUCT, null, where_clause, args, null, null, null);
        if(c != null){
            if(c.moveToFirst()){
                do{
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProduct_id(c.getInt(0));
                    product.setProductname(c.getString(1));
                    product.setManufacturer(c.getString(2));
                    product.setPrice(c.getInt(3));
                    product.setPosX(c.getInt(4));
                    product.setPosY(c.getInt(5));
                }while(c.moveToNext());
            }
        }
        c.close();
        return product;
    }
    public Product get_ProductFromDBbyID(String id){
        open_database();

        Product product = null;

        String where_clause = "product_id = ?";
        String[] args = {id};

        Cursor c = myDatabase.query(TBL_PRODUCT, null, where_clause, args, null, null, null);
        if(c != null){
            if(c.moveToFirst()){
                do{
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProduct_id(c.getInt(0));
                    product.setProductname(c.getString(1));
                    product.setManufacturer(c.getString(2));
                    product.setPrice(c.getInt(3));
                    product.setPosX(c.getInt(4));
                    product.setPosY(c.getInt(5));
                }while(c.moveToNext());
            }
        }
        Log.d("DBLOG", "productbyid");
        c.close();
        return product;
    }

    //insert full List to database
    public boolean insertList(Shopping_List list) {

        open_database();

        //write listvalues to database
        ContentValues values = new ContentValues();
        values.put("listname", list.getName());
        values.put("fk_user", list.getFk_user());

        //make transaction that only full list would be added or no list
        myDatabase.beginTransaction();
        try{
            long list_id = myDatabase.insert("LIST", null, values);
            if(list_id == -1){
                Log.d("DB_LOG","insert List Failure");
                return false;
            }
            list.setList_id(list_id);
            values.clear();

            for(Product p : list.getMyProducts()){

                setProductToList(get_ProductFromDB(p.getProductname(), p.getManufacturer()), list);
                Log.d("DB_LOG", p.getProductname() + " attached to "+list.getName()+ " on db");
            }
            myDatabase.setTransactionSuccessful();
            }catch(android.database.SQLException e){
                Log.d("DB_LOG", list.getName() + " could not be added to DB");
            }finally {
                myDatabase.endTransaction();
        }
        close_database();

        return true;
    }

    //match product to a List on database
    public boolean setProductToList(Product product, Shopping_List list){

        open_database();

        ContentValues values = new ContentValues();
        values.put("fk_product", product.getProduct_id());
        values.put("fk_list", list.getList_id());
        values.put("listposition", list.getMyProducts().indexOf(product));
        Log.d("LOG Productplatz:", ""+list.getMyProducts().indexOf(product));

        long list_prod_id = 0;

        try {
            list_prod_id = myDatabase.insert("LIST_PRODUCT", null, values);
        }catch(android.database.SQLException e) {
            Log.d("DB_LOG", product.getProductname() + " could not be added to attached to DB");
        }
        values.clear();
        close_database();
        return list_prod_id != -1;
    }

    public ArrayList<Shopping_List> getShoppingListsByUser(User user){

        open_database();

        ArrayList<Shopping_List> listArr = new ArrayList<>();
        Shopping_List list = null;

        String[] args = {""+user.getUser_id()};

        Cursor c = myDatabase.rawQuery("SELECT list_id, listname, user_id, budget FROM LIST " +
                "JOIN USER ON USER.user_id = LIST.fk_user " +
                "WHERE LIST.fk_user = ?", args);

        if(c != null){
            if(c.moveToFirst()){
                do{
                    list = new Shopping_List();
                    list.setList_id(c.getInt(0));
                    list.setName(c.getString(1));
                    list.setFk_user(c.getInt(2));
                    list.setBudget(c.getInt(3));
                    listArr.add(list);
                }while(c.moveToNext());
            }
        }
        c.close();
        close_database();
        return listArr;
    }

    public boolean deleteList(User user, Shopping_List clicked_list){
        open_database();

        Shopping_List list = getListById(clicked_list.getList_id());
        long list_id = list.getList_id();
        long id_check = user.getUser_id();

        //is user owner?
        if(id_check != list.getFk_user()){
            Log.d("LOG", "user is not owner of list");
            return false;
        }

        String[] args = {""+ list_id};
        open_database();
        myDatabase.beginTransaction();
        try {
            //deletes all entries from LIST_PRODUCT for the current list
            int check = myDatabase.delete(TBL_LIST_PRODUCT, "fk_list = ?", args);
            if (check == 0){
                Log.d("LOG", "No deletions from LIST_PRODUCT");
            }

            //deletes all entries from LIST_PSEUDO for the current list
            check = myDatabase.delete(TBL_LIST_PSEUDO, "fk_list = ?", args);
            if (check == 0){
                Log.d("LOG", "No deletions from LIST_PSEUDO");
            }

            //deletes all entries from RIGHT for the current list
            check = myDatabase.delete(TBL_RIGHT, "fk_list = ?", args);
            if (check == 0){
                Log.d("LOG", "No deletions from RIGHT");
            }

            //deletes current list in LIST
            check = myDatabase.delete(TBL_LIST, "list_id = ?", args);
            if (check == 0){
                Log.d("LOG", "No deletions from LIST");
            }
            myDatabase.setTransactionSuccessful();
        }catch(android.database.SQLException e){
            Log.d("DB_LOG", list.getName() + " could not be deleted from DB");
        }finally {
            myDatabase.endTransaction();
        }
        close_database();

        return true;
    }

    private Shopping_List getListById(long id){

        Shopping_List list = null;

        String[] args = {"" + id};

        Cursor c = myDatabase.rawQuery("SELECT * FROM LIST WHERE list_id = ?", args );

        if(c != null){
            if(c.moveToFirst()){
                list = new Shopping_List();
                list.setList_id(c.getInt(0));
                list.setName(c.getString(1));
                list.setFk_user(c.getInt(2));
                list.setBudget(c.getInt(3));
            }
        }
        c.close();
        list.setMyProducts(getAllProductsOfList(list));

        return  list;
    }

    //for the Product search
    public ArrayList<Product> getAllProductsBySearch(String searchTerm) {

        ArrayList<Product> recordsList = new ArrayList<Product>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + TBL_PRODUCT;
        sql += " WHERE productname LIKE '%" + searchTerm + "%' OR manufacturer LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY productname DESC";
        sql += " LIMIT 0,5";

        Log.d("LOG SQL-Statement: ", sql);

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                //String productName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                Product product = new Product("Dummy", "Dummy", 1);
                product.setProduct_id(c.getInt(0));
                product.setProductname(c.getString(1));
                product.setManufacturer(c.getString(2));
                product.setPrice(c.getInt(3));
                product.setPosX(c.getInt(4));
                product.setPosY(c.getInt(5));

                // add to list
                recordsList.add(product);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    public boolean deleteProductFromList(Product product, Shopping_List list){

        String[] args = {""+product.getProduct_id(), ""+list.getList_id()};
        open_database();

        //delete all entry from LIST_PRODUCT for the current product
        int check = myDatabase.delete(TBL_LIST_PRODUCT, "fk_product = ? AND fk_list = ?", args);
        if (check == 0){
            Log.d("LOG", "Deletion of product not possible");
            return false;
        }
        close_database();
        return true;

    }

    public void updateProductPositionsInList(Shopping_List list){

        open_database();

        ArrayList<Product> tmpList = list.getMyProducts();
        myDatabase.beginTransaction();
        try {
            for (Product product : tmpList) {
                ContentValues cv = new ContentValues();
                cv.put("listposition", "" + tmpList.indexOf(product));
                String[] args = {"" + list.getList_id(), "" + product.getProduct_id()};

                myDatabase.update(TBL_LIST_PRODUCT, cv, "fk_list = ? AND fk_product = ?", args);
            }
            myDatabase.setTransactionSuccessful();
        }catch(android.database.SQLException e){
            Log.d("DB_LOG", list.getName() + " could not update productposition");
        }finally {
            myDatabase.endTransaction();
        }
        close_database();
    }

    public void updateShoppingListName(Shopping_List list){

        open_database();

        ContentValues cv = new ContentValues();
        cv.put("listname", list.getName());
        String[] args = {""+list.getList_id()};
        myDatabase.update(TBL_LIST, cv, "list_id = ? ", args);

        close_database();
    }

    public void updateShoppingListBudget(Shopping_List list) {

        open_database();

        ContentValues cv = new ContentValues();
        cv.put("budget", list.getBudget());
        Log.d("LOGBudget",list.getBudget()+"");
        Log.d("LOGBudget",list.getList_id()+"");
        String[] args = {""+list.getList_id()};
        myDatabase.update(TBL_LIST, cv, "list_id = ? ", args);

        close_database();
    }
}