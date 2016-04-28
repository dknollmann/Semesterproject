package com.semesterdomain.semesterprojekt;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShoppingDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 36;
    private static final String DATABASE_NAME = "ShoppingDB";
    private static final String CREATE_DATABASE = "CREATE  TABLE IF NOT EXISTS USER ('user_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'username' TEXT NOT NULL UNIQUE, CHECK(typeof('username') = 'text' AND length('username') <= 100));";
    private static final String INSERT_USER = "INSERT INTO USER (username) VALUES ('Testuser');";


    ShoppingDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String insertFromFile(Context context, int resourceId) throws IOException {

        String dbCreation ="";

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            dbCreation += insertReader.readLine();
        }
        insertReader.close();

        // returning number of inserted rows
        return dbCreation;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
        db.execSQL(INSERT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_DATABASE);
        db.execSQL(INSERT_USER);
    }
}

