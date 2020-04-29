package com.pansala.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setSessionId(String id) {
        prefs.edit().putString("_id", id).commit();
    }

    public void setSessionFn(String name) {
        prefs.edit().putString("_name", name).commit();
    }

    public void setSessionType(String name) {
        prefs.edit().putString("_type", name).commit();
    }

    public String getSessionId() {
        String id = prefs.getString("_id","");
        return id;
    }

    public String getSessionFn() {
        String name = prefs.getString("_name","");
        return name;
    }

    public String getSessionType(){
        String name = prefs.getString("_type","");
        return name;
    }

    public void logout(){
        setSessionFn("");
        setSessionId("");
        setSessionType("");
    }
}
