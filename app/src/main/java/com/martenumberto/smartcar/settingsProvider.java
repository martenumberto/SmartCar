package com.martenumberto.smartcar;

import android.content.SharedPreferences;

/**
 * Created by marten on 08.12.16.
 */

public class settingsProvider {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public void setPrefString(String name, String value) {
        editor.putString(name, value);
    }

    public void setPrefInteger(String name, Integer value) {
        editor.putInt(name, value);
    }

    public void setPrefBoolean(String name, Boolean value) {
        editor.putBoolean(name, value);
    }

    public String getPrefString(String name) {
        return pref.getString(name, "FEHLER");
    }

    public Integer getPrefInteger(String name) {
        return pref.getInt(name, 0);
    }

    public Boolean getPrefBoolean(String name) {
        return pref.getBoolean(name, false);
    }
}
