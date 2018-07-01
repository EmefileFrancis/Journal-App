package com.emefilefrancis.journalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SP on 6/30/2018.
 */

public class SavedSharedPreference {
    public static String PREF_USERNAME_KEY = "username";
    public static String DEFAULT_PREF_USERNAME_KEY = "defaultUsername";

    public static void setPrefUsernameKey(Context context, String username){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PREF_USERNAME_KEY, username);
        editor.apply();
    }

    public static String getPrefUsernameKey(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_USERNAME_KEY, DEFAULT_PREF_USERNAME_KEY);
    }
}
