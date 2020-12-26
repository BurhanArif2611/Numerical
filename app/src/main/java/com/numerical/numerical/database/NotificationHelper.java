package com.numerical.numerical.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.numerical.numerical.Utility.ErrorMessage;

import java.util.ArrayList;

public class NotificationHelper {
    private static NotificationHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public NotificationHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }
    private boolean isExist(Notification_Model userDataModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + Notification_Model.TABLE_NAME + " where " + Notification_Model.KEY_id + "='" + userDataModel.getCollection_id() + "'", null);
        if (cur.moveToFirst()) {
            return true;
        }
        return false;
    }
    public static NotificationHelper getInstance() {
        return instance;
    }

    public void open() {
        db = dm.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void read() {
        db = dm.getReadableDatabase();
    }


    public void insertNotification_Model(Notification_Model userDataModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(Notification_Model.KEY_id, userDataModel.getCollection_id());
        values.put(Notification_Model.KEY_Title, userDataModel.getTitle());
        values.put(Notification_Model.KEY_Body, userDataModel.getBody());
        values.put(Notification_Model.KEY_Image_url, userDataModel.getImage_url());
        db.insert(Notification_Model.TABLE_NAME, null, values);
        /*if (!isExist(userDataModel)) {
            ErrorMessage.E("insert successfully");
            db.insert(Notification_Model.TABLE_NAME, null, values);
        } else {
            ErrorMessage.E("update successfully" + userDataModel.getCollection_id());
            db.update(Notification_Model.TABLE_NAME, values, Notification_Model.KEY_id + "=" + userDataModel.getCollection_id(), null);
        }*/
        close();
    }

    public ArrayList<Notification_Model> getNotification_Model() {
        ArrayList<Notification_Model> userItem = new ArrayList<Notification_Model>();
        read();
        Cursor clientCur = db.rawQuery("select * from "+Notification_Model.TABLE_NAME, null);
        if (clientCur != null && clientCur.getCount() > 0) {
            clientCur.moveToFirst();
            do {
                Notification_Model userDataModel = new Notification_Model();
                userDataModel.setCollection_id(clientCur.getString(clientCur.getColumnIndex(Notification_Model.KEY_id)));
                userDataModel.setBody(clientCur.getString(clientCur.getColumnIndex(Notification_Model.KEY_Body)));
                userDataModel.setTitle(clientCur.getString(clientCur.getColumnIndex(Notification_Model.KEY_Title)));
                userDataModel.setImage_url(clientCur.getString(clientCur.getColumnIndex(Notification_Model.KEY_Image_url)));
                userItem.add(userDataModel);
            } while ((clientCur.moveToNext()));
            clientCur.close();
        }
        close();
        return userItem;
    }
    public void delete() {
        try {
            open();
            db.delete(Notification_Model.TABLE_NAME, null, null);
            close();
        }catch (Exception e){}

    }
    public void deleteById(String id) {
        try {
            open();
            db.delete(Notification_Model.TABLE_NAME, Notification_Model.KEY_id + "=?" , new String[]{id}) ;
            close();
        }catch (Exception e){}

    }
}
