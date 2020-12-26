package com.numerical.numerical.database;

import android.database.sqlite.SQLiteDatabase;

public class Notification_Model {
    public static final String TABLE_NAME = "Notification";
    public static final String KEY_ID = "_id";
    public static final String KEY_id = "id";
    public static final String KEY_Title = "title";
    public static final String KEY_Body = "body";
    public static final String KEY_Image_url = "Image_Url";




    public static void creteTable(SQLiteDatabase db) {
        String CREATE_STUDENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_id + " text, "
                + KEY_Title + " text, "
                + KEY_Body + " text, "
                + KEY_Image_url + " text " +
                ")";
        db.execSQL(CREATE_STUDENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    String collection_id,title,body,image_url;

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
