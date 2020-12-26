package com.numerical.numerical.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataManager extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 15;
    public static final String DATABASE_NAME = "Numerical";

    public DataManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserProfileModel.creteTable(db);
        Notification_Model.creteTable(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2) {
        UserProfileModel.dropTable(db);
        Notification_Model.dropTable(db);

        onCreate(db);
    }


}
