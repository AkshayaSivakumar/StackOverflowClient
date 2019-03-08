package com.example.stackoverflowclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static PreferenceHelper preference;
    private SharedPreferences sharedPreferences;
    private static String fileName = "SOCPreference";
    private SharedPreferences.Editor editor;

    public PreferenceHelper(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance(Context context) {
        if (null == preference)
            preference = new PreferenceHelper(context);

        return preference;
    }

    public void put(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putInt(String key, int value) {
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void clear() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void remove() {
        editor = sharedPreferences.edit();
        editor.remove(fileName);
        editor.commit();
    }
}
