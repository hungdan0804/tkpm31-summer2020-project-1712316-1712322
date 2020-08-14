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
    private static final String TOTALLIFETIME = "totalLifeTime";
    private static final String TODAYLIFETIME = "todayLifeTime";
    private static final String FLAT_EVERYDAY_SERVICE = "FLAT-EVERYDAY-SERVICE";


    public CurrentUser(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFlatEverydayService(boolean value){
        editor.putBoolean(FLAT_EVERYDAY_SERVICE,value);
        editor.commit();
    }

    public void setCurrentUser(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public void setTotallifetime(long totallifetime) {
        editor.putLong(TOTALLIFETIME, totallifetime);
        editor.commit();
    }

    public void setTodaylifetime(long todaylifetime) {
        editor.putLong(TODAYLIFETIME, todaylifetime);
        editor.commit();
    }


    public String getCurrentUser() {
        return pref.getString(USERNAME, null);
    }

    public long getTotalLifeTime() {
        return pref.getLong(TOTALLIFETIME, 0);
    }

    public long getTodayLifeTime() {
        return pref.getLong(TODAYLIFETIME, 0);
    }

    public boolean getFlatEverydayService(){return pref.getBoolean(FLAT_EVERYDAY_SERVICE,false);}
}
