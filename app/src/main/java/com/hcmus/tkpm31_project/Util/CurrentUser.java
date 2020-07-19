package com.hcmus.tkpm31_project.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "CUR-USER";

    private static final String USERNAME = "username";

    public CurrentUser(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setCurrentUser(String username) {
        editor.putString(USERNAME,username);
        editor.commit();
    }

    public String getCurrentUser() {
        return pref.getString(USERNAME,null);
    }
}