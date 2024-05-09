package com.example.livechat.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.livechat.activity.MainActivity;
import com.example.livechat.activity.chat.ChatActivity;
import com.example.livechat.activity.login.LoginActivity;
import com.example.livechat.activity.users.UsersActivity;
import com.example.livechat.model.UserModel;

public class SessionManagement {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context _context;

    private static final String KEY_TOKEN = "idToken";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String IS_LOGIN = "IsLoggedIn";


    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private int PRIVATE_MODE = 0;

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(UserModel userModel){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing token in pref
        editor.putString(KEY_TOKEN, userModel.getToken());
        editor.putString(KEY_ID, userModel.getID());
        editor.putString(KEY_NAME, userModel.getName());
        editor.putString(KEY_EMAIL, userModel.getEmail());

        // commit changes
        editor.commit();
    }

    public String getSessionToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public String getUsername() {
        return pref.getString(KEY_NAME, null);
    }

    public String getID() {
        return pref.getString(KEY_ID, null);
    }

    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, UsersActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }
}
