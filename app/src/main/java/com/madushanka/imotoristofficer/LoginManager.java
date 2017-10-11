package com.madushanka.imotoristofficer;

import android.content.SharedPreferences;

import com.madushanka.imotoristofficer.entities.Login;


public class LoginManager {

    private static LoginManager INSTANCE = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private LoginManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    static synchronized LoginManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new LoginManager(prefs);
        }
        return INSTANCE;
    }

    public void saveLogin(Login l) {
        editor.putString("username", l.getUsername()).commit();
        editor.putString("password", l.getPassword()).commit();
    }

    public void deleteLogin() {
        editor.remove("username").commit();
        editor.remove("password").commit();
    }

    public Login getLogin() {
        Login l = new Login();
        l.setUsername(prefs.getString("username", null));
        l.setPassword(prefs.getString("password", null));
        return l;
    }


}
