package com.example.nowshadhasan.databaselab.another;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nowshad on 1/11/17.
 */

public class PrefManager {

    SharedPreferences preferences;
    Context _context;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME="welcomePref";

    public PrefManager(Context context){
        this._context=context;
        preferences=_context.getSharedPreferences(PREF_NAME,0);
        editor=preferences.edit();
    }

    public void setFirstTimeLaunch(boolean firstTime){
        editor.putBoolean("FirstTimeLaunch",firstTime);
        editor.commit();
    }
    public boolean getFirstTimeLaunch(){
        return preferences.getBoolean("FirstTimeLaunch",true);
    }
}
