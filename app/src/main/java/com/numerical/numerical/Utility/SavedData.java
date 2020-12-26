package com.numerical.numerical.Utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.numerical.numerical.MyApplication;

public class SavedData {

    private static final String PASSWORD = "passsword";
    private static final String TOKAN = "tokan";
    private static final String Check = "check";
    private static final String ACCOUNT = "account";
    private static final String AddToCart_Count = "addtocart";
    private static final String Topic_position = "Topic_position";
    private static final String Cat_id = "Cat_id";


    static SharedPreferences prefs;

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        }
        return prefs;
    }

    public static String getPassword() {
        return getInstance().getString(PASSWORD, "");
    }

    public static void savePassword(String passsword) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(PASSWORD, passsword);
        editor.apply();
    }

    public static String getTokan() {
        return getInstance().getString(TOKAN, "");
    }

    public static void saveTokan(String tokan) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(TOKAN, tokan);
        editor.apply();
    }
    public static String getAddToCart_Count() {
        return getInstance().getString(AddToCart_Count, "0");
    }

    public static void saveAddToCart_Count(String fg) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AddToCart_Count, fg);
        editor.apply();
    }

    public static String getTopic_position() {
        return getInstance().getString(Topic_position, "-1");
    }

    public static void saveTopic_position(String fg) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(Topic_position, fg);
        editor.apply();
    }
    public static String getCat_id() {
        return getInstance().getString(Cat_id, "");
    }

    public static void saveCat_id(String fg) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(Cat_id, fg);
        editor.apply();
    }

}