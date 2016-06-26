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


/**
 * The type SQLiteDBHelper handles connecting, copying, checking, opening and closing the SQLite DB
 * on the useres device. The SQLiteDBHelper is also used for storing, deleting and
 * updating DB records.
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {

    /**
     * The constant DATABASE_VERSION is only used for the super constructor from
     * the SQLiteOpenHelper.
     */

    private static final int DATABASE_VERSION = 1;
    /**
     * The constant BUFFER_SIZE is used for coping the DB.
     */
    private static final int BUFFER_SIZE = 1024;
    /**
     * The constant DB_NAME simply stores the filename of the SQLite DB which is used for App.
     */
    private static final String DB_NAME = "ShoppingDB_32k.sqlite";
    /**
     * The constant DB_PATH stores the filepath to the SQLite DB file which should be loaded.
     */
    private static final String DB_PATH = "/data/data/com.semesterdomain.semesterprojekt/databases/";

    /**
     * The constant TBL_PRODUCT.
     */

    private static final String TBL_PRODUCT = "PRODUCT";
    /**
     * The constant TBL_LIST.
     */
    private static final String TBL_LIST = "LIST";
    /**
     * The constant TBL_USER.
     */
    private static final String TBL_USER = "USER";
    /**
     * The constant TBL_PSEUDO.
     */
    private static final String TBL_PSEUDO = "PSEUDO";
    /**
     * The constant TBL_RIGHT.
     */
    private static final String TBL_RIGHT = "RIGHT";
    /**
     * The constant TBL_LIST_PRODUCT.
     */
    private static final String TBL_LIST_PRODUCT = "LIST_PRODUCT";
    /**
     * The constant TBL_LIST_PSEUDO.
     */
    private static final String TBL_LIST_PSEUDO = "LIST_PSEUDO";


    /**
     * The Db helper context.
     */
    private final Context dbHelperContext;
    /**
     * The SQLiteDatabase is used in many other classed for operating with the DB.
     */
    private SQLiteDatabase SQLiteDatabase;


    /**
     * Instantiates a new SQLiteDBHelper.
     *
     * @param context the context
     */
    SQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.dbHelperContext = context;
    }

    /**
     * Imports the DB and if the databasefile exists the DB will be copied.
     */
    public void importDatabase() {

        if (checkDatabaseFile()) {
            //Log.d("DB_LOG", "Database already on device");
        } else {
            this.getReadableDatabase();
            copyDatabase();
        }

    }

    /**
     * Checks if the databasefile exists.
     *
     * @return the boolean
     */
    public boolean checkDatabaseFile() {
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }

    /**
     * Copys the database using streams.
     */
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

    /**
     * Opens the databse and checks if the database is already open if the database object is null.
     */
    public void openDatabase() {
        if (!(SQLiteDatabase != null && SQLiteDatabase.isOpen())) {
            SQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            //Log.d("LOG", DB_NAME + " opened");
        }
    }

    /**
     * Closes the database and checks if the database object is null.
     */
    public void closeDatabase() {
        if (SQLiteDatabase != null) {
            SQLiteDatabase.close();
            //Log.d("LOG", DB_NAME + " closed");
        }
    }

    /**
     * Not implemented.
     *
     * @param db the db.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * Not implemented.
     *
     * @param db         the db.
     * @param oldVersion the old version.
     * @param newVersion the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //increase Version with every update
    }

    /**
     * Gets all the products for a single shoppinglist from the DB.
     *
     * @param list the list from which the products should be queried.
     * @return the queried products.
     */
    public ArrayList<Product> getDBProductsFromList(ShoppingList list) {
        openDatabase();

        Product product;
        String[] args = {String.valueOf(list.getListId())};

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

    /**
     * Gets potentially multiple products from the DB and they are searched by their productName
     * and manufacturer.
     *
     * @param productname  the productname which is used to search products.
     * @param manufacturer the manufacturer of a product wich is used to search products.
     * @return the queried products.
     */

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

    /**
     * Gets a single product by its id from the DB.
     *
     * @param id the id of the searched product.
     * @return the queried product.
     */
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

    /**
     * Adds a user created shoppinglist and all the products in it to the DB. To avoid an
     * inconsistent DB the is done ins an transaction.
     *
     * @param list the shoppinglist which should be added.
     * @return the boolean is false when the transaction failed.
     */

    public boolean addDBList(ShoppingList list) {

        openDatabase();
        ContentValues values = new ContentValues();
        values.put("listname", list.getName());
        values.put("fk_user", list.getFkUser());

        //starting a transaction that only all elements or none of elements at all get added.
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

                addDBProductToList(getDBProductByProductNameAndManufaturer(p.getProductName(), p.getManufacturer()), list);
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

    /**
     * Adds a product to the shoppinglist inside the DB.
     *
     * @param product the product which should be added.
     * @param list    the list where the products should be added to.
     * @return is false when the insert of the products failed.
     */

    public boolean addDBProductToList(Product product, ShoppingList list) {

        openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("fk_product", product.getProductId());
        contentValues.put("fk_list", list.getListId());
        contentValues.put("listposition", list.getMyProducts().indexOf(product));
        //Log.d("LOG Productplatz:", "" + list.getMyProducts().indexOf(product));

        long listProdId = 0;

        try {
            listProdId = SQLiteDatabase.insert("LIST_PRODUCT", null, contentValues);
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", product.getProductName() + " could not be added to attached to DB");
        }
        contentValues.clear();
        closeDatabase();
        return listProdId != -1;
    }

    /**
     * Gets all shoppinglists for a users from the DB, which is done with a query based on
     * the users ID.
     *
     * @param user the user for the query based on his ID.
     * @return the queried shoppinglists.
     */
    public ArrayList<ShoppingList> getDBShoppingListsByUser(User user) {

        openDatabase();

        ArrayList<ShoppingList> shoppingArrayList = new ArrayList<>();
        ShoppingList shoppingList = null;

        String[] args = {String.valueOf(user.getUserId())};

        Cursor dbCursor = SQLiteDatabase.rawQuery("SELECT list_id, listname, user_id, budget FROM LIST " +
                "JOIN USER ON USER.user_id = LIST.fk_user " +
                "WHERE LIST.fk_user = ?" +
                "ORDER BY listname ASC", args);

        if (dbCursor != null) {
            if (dbCursor.moveToFirst()) {
                do {
                    shoppingList = new ShoppingList();
                    shoppingList.setListId(dbCursor.getInt(0));
                    shoppingList.setName(dbCursor.getString(1));
                    shoppingList.setFkUser(dbCursor.getInt(2));
                    shoppingList.setBudget(dbCursor.getInt(3));
                    shoppingArrayList.add(shoppingList);
                } while (dbCursor.moveToNext());
            }
        }
        dbCursor.close();
        closeDatabase();
        return shoppingArrayList;
    }

    /**
     * Deletes a shoppinglist and the references in the other tables (LIST_PRODUCT, LIST_PSEUDO and
     * RIGHT) from the DB. For this operation the owner (user) of the shoppinglist is needed
     * because only the owner is supposed to delete his own shoppinglists.
     *
     * @param user        the user who might be the rightfull owner of the shoppinglist.
     * @param ToBeDeletedList the shoppinglist which gets removed from the DB.
     * @return the boolean is false when the user is not the owner.
     */
    public boolean deleteDBList(User user, ShoppingList ToBeDeletedList) {
        openDatabase();

        ShoppingList list = getDBListById(ToBeDeletedList.getListId());
        long listId = list.getListId();
        long idCheck = user.getUserId();

        //is the user the owner of the list?
        if (idCheck != list.getFkUser()) {
            //Log.d("LOG", "user is not owner of list");
            return false;
        }

        String[] args = {String.valueOf(listId)};
        openDatabase();
        SQLiteDatabase.beginTransaction();
        try {
            //deletes all entries from LIST_PRODUCT for the current shoppinglist
            int check = SQLiteDatabase.delete(TBL_LIST_PRODUCT, "fk_list = ?", args);
            if (check == 0) {
                //Log.d("LOG", "No deletions from LIST_PRODUCT");
            }

            //deletes all entries from LIST_PSEUDO for the current shoppinglist
            check = SQLiteDatabase.delete(TBL_LIST_PSEUDO, "fk_list = ?", args);
            if (check == 0) {
                //Log.d("DB_LOG", "No deletions from LIST_PSEUDO.");
            }

            //deletes all entries from RIGHT for the current shoppinglist
            check = SQLiteDatabase.delete(TBL_RIGHT, "fk_list = ?", args);
            if (check == 0) {
                //Log.d("DB_LOG", "No deletions from RIGHT.");
            }

            //deletes current shoppinglist in LIST
            check = SQLiteDatabase.delete(TBL_LIST, "list_id = ?", args);
            if (check == 0) {
                //Log.d("DB_LOG", "No deletions from LIST.");
            }
            SQLiteDatabase.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", list.getName() + " could not be deleted from DB.");
        } finally {
            SQLiteDatabase.endTransaction();
        }
        closeDatabase();

        return true;
    }

    /**
     * Gets a shoppinglist from the DB which is queried with its own ID.
     *
     * @param id the ID of the shoppinglist
     * @return the shoppinglist which got queried with the ID given to this method.
     */
    private ShoppingList getDBListById(long id) {

        ShoppingList shoppingList = null;

        String[] args = {String.valueOf(id)};

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

    /**
     * Gets products from the DB queried with the searchTerm parameter. The products are queried by
     * a LIKE query for the productName and the manufacturer of the product.
     *
     * @param searchTerm the search string which should be used to query the productName and the manufacturer.
     * @return a ArrayList with all queried products.
     */

    public ArrayList<Product> getDBProductsBySearchTerm(String searchTerm) {

        ArrayList<Product> queriedProducts = new ArrayList<Product>();

        //build sql query
        String sql = "";
        sql += "SELECT * FROM " + TBL_PRODUCT;
        sql += " WHERE productname LIKE '%" + searchTerm + "%' OR manufacturer LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY productname DESC";
        sql += " LIMIT 0,5";
        //Log.d("DB_LOG", "SQL-Statement: " + sql);

        SQLiteDatabase db = this.getWritableDatabase();

        //execute the query
        Cursor dbCursor = db.rawQuery(sql, null);

        //looping through all rows and adding to list
        if (dbCursor.moveToFirst()) {
            do {
                //int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                //String productName = cursor.getString(cursor.getColumnIndex(fieldObjectName));

                //build the products from the query's return.
                Product product = new Product("Dummy", "Dummy", 1);
                product.setProductId(dbCursor.getInt(0));
                product.setProductName(dbCursor.getString(1));
                product.setManufacturer(dbCursor.getString(2));
                product.setPrice(dbCursor.getInt(3));
                product.setPosX(dbCursor.getInt(4));
                product.setPosY(dbCursor.getInt(5));
                //add products to list
                queriedProducts.add(product);

            } while (dbCursor.moveToNext());
        }

        dbCursor.close();
        db.close();
        // return the list of records
        return queriedProducts;
    }

    /**
     * Deletes a single products from the DB record for a single shoppinglist.
     *
     * @param product the product
     * @param shoppingList    the shoppinglist where the product should be deleted from.
     * @return the boolean indicates if the deletion was succesfull or not.
     */
    public boolean deleteDBProductFromList(Product product, ShoppingList shoppingList) {

        String[] args = {String.valueOf(product.getProductId()), String.valueOf(shoppingList.getListId())};
        openDatabase();

        //delete all entry from LIST_PRODUCT for the current product
        int check = SQLiteDatabase.delete(TBL_LIST_PRODUCT, "fk_product = ? AND fk_list = ?", args);
        if (check == 0) {
            Log.d("DB_LOG", "Deletion of product failed.");
            return false;
        }
        closeDatabase();
        return true;

    }

    /**
     * Updates the product position for all products of a single shoppinglist. This is done in an
     * transaction because all positions should be updated or none to avoide inconsistent DB records.
     *
     * @param shoppingList the shoppinglist where the products positions should be updated.
     */
    public void updateDBProductPositionsInList(ShoppingList shoppingList) {

        openDatabase();

        ArrayList<Product> tmpList = shoppingList.getMyProducts();
        SQLiteDatabase.beginTransaction();
        try {
            for (Product product : tmpList) {
                ContentValues cv = new ContentValues();
                cv.put("listposition", String.valueOf(tmpList.indexOf(product)));
                String[] args = {String.valueOf(shoppingList.getListId()), String.valueOf(product.getProductId())};

                SQLiteDatabase.update(TBL_LIST_PRODUCT, cv, "fk_list = ? AND fk_product = ?", args);
            }
            SQLiteDatabase.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            Log.d("DB_LOG", "Failed to update productposition of the shoppinglist: " + shoppingList.getName());
        } finally {
            SQLiteDatabase.endTransaction();
        }
        closeDatabase();
    }

    /**
     * Updates the name of the single shoppinglist.
     *
     * @param shoppingList the shoppinglist of which the name should be updated.
     */
    public void updateDBShoppingListName(ShoppingList shoppingList) {

        openDatabase();

        ContentValues cv = new ContentValues();
        cv.put("listname", shoppingList.getName());
        String[] args = {String.valueOf(shoppingList.getListId())};
        SQLiteDatabase.update(TBL_LIST, cv, "list_id = ? ", args);

        closeDatabase();
    }

    /**
     * Updates budget for a single shoppinglist.
     *
     * @param shoppingList the shoppinglist of which the budget should be updated.
     */
    public void updateDBBudgetForList(ShoppingList shoppingList) {

        openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("budget", shoppingList.getBudget());
        //Log.d("LOGBudget", list.getBudget() + "");
        //Log.d("LOGBudget", list.getListId() + "");
        String[] args = {String.valueOf(shoppingList.getListId())};
        SQLiteDatabase.update(TBL_LIST, contentValues, "list_id = ? ", args);

        closeDatabase();
    }
}