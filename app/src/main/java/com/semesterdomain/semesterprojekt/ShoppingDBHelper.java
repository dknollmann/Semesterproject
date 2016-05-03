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


public class ShoppingDBHelper extends SQLiteOpenHelper {

    //DB Data
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "ShoppingDB.sqlite";
    private static final String DB_PATH = "/data/data/com.semesterdomain.semesterprojekt/databases/";

    //Tables
    private static final String TBL_PRODUCT = "PRODUCT";
    private static final String TBL_LIST = "LIST";
    private static final String TBL_USER = "USER";
    private static final String TBL_PSEUDO = "PSEUDO";
    private static final String TBL_RIGHT = "RIGHT";


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
        if(file.exists()){
            return true;
        }
        else{
            return false;
        }
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
        myDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        Log.d("LOG",DB_NAME +" opened");
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

    //get Single Productdata
    public Product get_ProductFromDB(String productname, String manufacturer){
        if(!myDatabase.isOpen()){
            open_database();
        }
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

    public boolean insertList(Shopping_List list) {
        open_database();

        //write listvalues to database
        ContentValues values = new ContentValues();
        values.put("listname", list.getName());
        values.put("fk_user", list.getFk_user());
        //evtl try-catch weil methode wirft SQLExepprion wenns der insert fehlschlägt
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

                setProductToList(get_ProductFromDB(p.getProductname(), p.getManufacturer()), list_id);
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
    public boolean setProductToList(Product product, long id){

        ContentValues values = new ContentValues();
        values.put("fk_product", product.getProduct_id());
        values.put("fk_list", id);
        long list_prod_id = 0;

        //myDatabase.beginTransaction();
        try {
            list_prod_id = myDatabase.insert("LIST_PRODUCT", null, values);
        }catch(android.database.SQLException e) {
            Log.d("DB_LOG", product.getProductname() + " could not be added to attached to DB");
        }finally {
            //myDatabase.endTransaction();
        }
        values.clear();
        if(list_prod_id == -1){
            return false;
        }
        return true;
    }

    public Shopping_List getShoppingList(String listname){
        if(!myDatabase.isOpen()){
            open_database();
        }
        Shopping_List list = null;

        String where_clause = "listname = ?";
        String[] args = {listname};

        Cursor c = myDatabase.query(TBL_LIST, null, where_clause, args, null, null, null);
        if(c != null){
            if(c.moveToFirst()){
                do{
                    list = new Shopping_List();
                    list.setList_id(c.getInt(0));
                    list.setName(listname);
                    list.setFk_user(c.getInt(2));
                }while(c.moveToNext());
            }
        }
        c.close();
        close_database();
        return list;
    }
}

