package com.madushanka.imotoristofficer.controllers;

import android.content.SharedPreferences;

import com.madushanka.imotoristofficer.entities.User;


public class UserManager {

    private static UserManager INSTANCE = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private UserManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized UserManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new UserManager(prefs);
        }
        return INSTANCE;
    }

    public void saveUser(User u) {
        editor.putString("id", u.getId()+"".trim()).commit();
        editor.putString("name", u.getName()).commit();
        editor.putString("email", u.getEmail()).commit();

    }

    public void deleteUser() {
        editor.remove("id").commit();
        editor.remove("name").commit();
        editor.remove("email").commit();
    }

    public User getUser() {
        User u = new User();
        u.setId(prefs.getString("id", null));
        u.setName(prefs.getString("name", null));
        u.setEmail(prefs.getString("email", null));
        return u;
    }


}
