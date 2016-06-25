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
import java.util.ArrayList;


public class SQLiteDBHelper extends SQLiteOpenHelper {

    //DB Data
    private static final int DATABASE_VERSION = 1;
    private static final int BUFFER_SIZE = 1024;
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


    private final Context dbHelperContext;
    private SQLiteDatabase SQLiteDatabase;


    SQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.dbHelperContext = context;
    }

    public void importDatabase() {

        if (checkDatabaseFile()) {
            //Log.d("DB_LOG", "Database already on device");
        } else {
            this.getReadableDatabase();
            copyDatabase();
        }

    }

    public boolean checkDatabaseFile() {
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }

    public void copyDatabase() {

        try {
            InputStream is = dbHelperContext.getResources().getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            Log.d("DB_LOG", "Copy Failure");
            e.printStackTrace();
        }
        //Log.d("LOG", "Database copied on device");
    }

    public void openDatabase() {
        if (!(SQLiteDatabase != null && SQLiteDatabase.isOpen())) {
            SQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            //Log.d("LOG", DB_NAME + " opened");
        }
    }

    public void closeDatabase() {
        if (SQLiteDatabase != null) {
            SQLiteDatabase.close();
            //Log.d("LOG", DB_NAME + " closed");
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

    public ArrayList<Product> getDBProductsFromList(ShoppingList list) {
        openDatabase();

        Product product;
        String[] args = {"" + list.getListId()};

        Cursor dbCursor = SQLiteDatabase.rawQuery("SELECT product_id, productname, manufacturer, productprice, posx, posy, listposition FROM LIST_PRODUCT " +
                "JOIN PRODUCT ON LIST_PRODUCT.fk_product = PRODUCT.product_id " +
                "WHERE LIST_PRODUCT.fk_list = ?" +
                "ORDER BY listposition ASC", args);
        list.getMyProducts().clear();
        if (dbCursor != null) {
            /*c.getCount();
            ArrayList<Product> al = new ArrayList<>();
            */
            if (dbCursor.moveToFirst()) {
                do {
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProductId(dbCursor.getInt(0));
                    product.setProductName(dbCursor.getString(1));
                    product.setManufacturer(dbCursor.getString(2));
                    product.setPrice(dbCursor.getInt(3));
                    product.setPosX(dbCursor.getInt(4));
                    product.setPosY(dbCursor.getInt(5));
                    //Log.d("LOGBla", "" + c.getInt(6));
                    list.getMyProducts().add(product);
                } while (dbCursor.moveToNext());
            }
        }
        dbCursor.close();
        closeDatabase();
        return list.getMyProducts();
    }

    //get Single Productdata
    public Product getDBProductByProductNameAndManufaturer(String productname, String manufacturer) {
        openDatabase();

        Product product = null;

        String whereClause = "productname = ? AND manufacturer = ?";
        String[] args = {productname, manufacturer};

        Cursor c = SQLiteDatabase.query(TBL_PRODUCT, null, whereClause, args, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProductId(c.getInt(0));
                    product.setProductName(c.getString(1));
                    product.setManufacturer(c.getString(2));
                    product.setPrice(c.getInt(3));
                    product.setPosX(c.getInt(4));
                    product.setPosY(c.getInt(5));
                } while (c.moveToNext());
            }
        }
        c.close();
        return product;
    }

    public Product getDBProductById(String id) {
        openDatabase();

        Product product = null;

        String whereClause = "product_id = ?";
        String[] args = {id};

        Cursor dbCursor = SQLiteDatabase.query(TBL_PRODUCT, null, whereClause, args, null, null, null);
        if (dbCursor != null) {
            if (dbCursor.moveToFirst()) {
                do {
                    product = new Product("Dummy", "Dummy", 1);
                    product.setProductId(dbCursor.getInt(0));
                    product.setProductName(dbCursor.getString(1));
                    product.setManufacturer(dbCursor.getString(2));
                    product.setPrice(dbCursor.getInt(3));
                    product.setPosX(dbCursor.getInt(4));
                    product.setPosY(dbCursor.getInt(5));
                } while (dbCursor.moveToNext());
            }
        }
        //Log.d("DBLOG", "productbyid");
        dbCursor.close();
        return product;
    }

    //insert full List to database
    public boolean insertList(ShoppingList list) {

        openDatabase();

        //write listvalues to database
        ContentValues values = new ContentValues();
        values.put("listname", list.getName());
        values.put("fk_user", list.getFkUser());

        //make transaction that only full list would be added or no list
        SQLiteDatabase.beginTransaction();
        try {
            long listId = SQLiteDatabase.insert("LIST", null, values);
            if (listId == -1) {
                //Log.d("DB_LOG", "insert List Failure");
                return false;
            }
            list.setListId(listId);
            values.clear();

            for (Product p : list.getMyProducts()) {

                setProductToList(getDBProductByProductNameAndManufaturer(p.getProductName(), p.getManufacturer()), list);
                //Log.d("DB_LOG", p.getProductName() + " attached to " + list.getName() + " on db");
            }
            SQLiteDatabase.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", list.getName() + " could not be added to DB");
        } finally {
            SQLiteDatabase.endTransaction();
        }
        closeDatabase();

        return true;
    }

    //match product to a List on database
    public boolean setProductToList(Product product, ShoppingList list) {

        openDatabase();

        ContentValues values = new ContentValues();
        values.put("fk_product", product.getProductId());
        values.put("fk_list", list.getListId());
        values.put("listposition", list.getMyProducts().indexOf(product));
        //Log.d("LOG Productplatz:", "" + list.getMyProducts().indexOf(product));

        long listProdId = 0;

        try {
            listProdId = SQLiteDatabase.insert("LIST_PRODUCT", null, values);
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", product.getProductName() + " could not be added to attached to DB");
        }
        values.clear();
        closeDatabase();
        return listProdId != -1;
    }

    public ArrayList<ShoppingList> getShoppingListsByUser(User user) {

        openDatabase();

        ArrayList<ShoppingList> shoppingArrayList = new ArrayList<>();
        ShoppingList shoppingList = null;

        String[] args = {"" + user.getUserId()};

        Cursor dbCurser = SQLiteDatabase.rawQuery("SELECT list_id, listname, user_id, budget FROM LIST " +
                "JOIN USER ON USER.user_id = LIST.fk_user " +
                "WHERE LIST.fk_user = ?", args);

        if (dbCurser != null) {
            if (dbCurser.moveToFirst()) {
                do {
                    shoppingList = new ShoppingList();
                    shoppingList.setListId(dbCurser.getInt(0));
                    shoppingList.setName(dbCurser.getString(1));
                    shoppingList.setFkUser(dbCurser.getInt(2));
                    shoppingList.setBudget(dbCurser.getInt(3));
                    shoppingArrayList.add(shoppingList);
                } while (dbCurser.moveToNext());
            }
        }
        dbCurser.close();
        closeDatabase();
        return shoppingArrayList;
    }

    public boolean deleteDBList(User user, ShoppingList clickedList) {
        openDatabase();

        ShoppingList list = getDBListById(clickedList.getListId());
        long listId = list.getListId();
        long idCheck = user.getUserId();

        //is user owner?
        if (idCheck != list.getFkUser()) {
            //Log.d("LOG", "user is not owner of list");
            return false;
        }

        String[] args = {"" + listId};
        openDatabase();
        SQLiteDatabase.beginTransaction();
        try {
            //deletes all entries from LIST_PRODUCT for the current list
            int check = SQLiteDatabase.delete(TBL_LIST_PRODUCT, "fk_list = ?", args);
            if (check == 0) {
                //Log.d("LOG", "No deletions from LIST_PRODUCT");
            }

            //deletes all entries from LIST_PSEUDO for the current list
            check = SQLiteDatabase.delete(TBL_LIST_PSEUDO, "fk_list = ?", args);
            if (check == 0) {
                Log.d("DB_LOG", "No deletions from LIST_PSEUDO");
            }

            //deletes all entries from RIGHT for the current list
            check = SQLiteDatabase.delete(TBL_RIGHT, "fk_list = ?", args);
            if (check == 0) {
                Log.d("DB_LOG", "No deletions from RIGHT");
            }

            //deletes current list in LIST
            check = SQLiteDatabase.delete(TBL_LIST, "list_id = ?", args);
            if (check == 0) {
                Log.d("DB_LOG", "No deletions from LIST");
            }
            SQLiteDatabase.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", list.getName() + " could not be deleted from DB");
        } finally {
            SQLiteDatabase.endTransaction();
        }
        closeDatabase();

        return true;
    }

    private ShoppingList getDBListById(long id) {

        ShoppingList shoppingList = null;

        String[] args = {"" + id};

        Cursor dbCursor = SQLiteDatabase.rawQuery("SELECT * FROM LIST WHERE list_id = ?", args);

        if (dbCursor != null) {
            if (dbCursor.moveToFirst()) {
                shoppingList = new ShoppingList();
                shoppingList.setListId(dbCursor.getInt(0));
                shoppingList.setName(dbCursor.getString(1));
                shoppingList.setFkUser(dbCursor.getInt(2));
                shoppingList.setBudget(dbCursor.getInt(3));
            }
        }
        dbCursor.close();
        shoppingList.setMyProducts(getDBProductsFromList(shoppingList));

        return shoppingList;
    }

    //for the Product search
    public ArrayList<Product> getDBProductsBySearchTerm(String searchTerm) {

        ArrayList<Product> recordsList = new ArrayList<Product>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + TBL_PRODUCT;
        sql += " WHERE productname LIKE '%" + searchTerm + "%' OR manufacturer LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY productname DESC";
        sql += " LIMIT 0,5";

        //Log.d("LOG SQL-Statement: ", sql);

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor dbCursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (dbCursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                //String productName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                Product product = new Product("Dummy", "Dummy", 1);
                product.setProductId(dbCursor.getInt(0));
                product.setProductName(dbCursor.getString(1));
                product.setManufacturer(dbCursor.getString(2));
                product.setPrice(dbCursor.getInt(3));
                product.setPosX(dbCursor.getInt(4));
                product.setPosY(dbCursor.getInt(5));

                // add to list
                recordsList.add(product);

            } while (dbCursor.moveToNext());
        }

        dbCursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    public boolean deleteProductFromList(Product product, ShoppingList list) {

        String[] args = {"" + product.getProductId(), "" + list.getListId()};
        openDatabase();

        //delete all entry from LIST_PRODUCT for the current product
        int check = SQLiteDatabase.delete(TBL_LIST_PRODUCT, "fk_product = ? AND fk_list = ?", args);
        if (check == 0) {
            Log.d("DB_LOG", "Deletion of product not possible");
            return false;
        }
        closeDatabase();
        return true;

    }

    public void updateProductPositionsInList(ShoppingList list) {

        openDatabase();

        ArrayList<Product> tmpList = list.getMyProducts();
        SQLiteDatabase.beginTransaction();
        try {
            for (Product product : tmpList) {
                ContentValues cv = new ContentValues();
                cv.put("listposition", "" + tmpList.indexOf(product));
                String[] args = {"" + list.getListId(), "" + product.getProductId()};

                SQLiteDatabase.update(TBL_LIST_PRODUCT, cv, "fk_list = ? AND fk_product = ?", args);
            }
            SQLiteDatabase.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", list.getName() + " could not update productposition");
        } finally {
            SQLiteDatabase.endTransaction();
        }
        closeDatabase();
    }

    public void updateShoppingListName(ShoppingList shoppingList) {

        openDatabase();

        ContentValues cv = new ContentValues();
        cv.put("listname", shoppingList.getName());
        String[] args = {"" + shoppingList.getListId()};
        SQLiteDatabase.update(TBL_LIST, cv, "list_id = ? ", args);

        closeDatabase();
    }

    public void updateDBBudgetForList(ShoppingList shoppingList) {

        openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("budget", shoppingList.getBudget());
        //Log.d("LOGBudget", list.getBudget() + "");
        //Log.d("LOGBudget", list.getListId() + "");
        String[] args = {"" + shoppingList.getListId()};
        SQLiteDatabase.update(TBL_LIST, contentValues, "list_id = ? ", args);

        closeDatabase();
    }
}