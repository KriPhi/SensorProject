package com.vu.lorasensorapp;

import android.content.Context;
import android.content.SharedPreferences;

public class adminPreference {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isAdmin(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_ADMIN, false);
    }

    public static void setIsAdmin(Context context, boolean isAdmin) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }
}
