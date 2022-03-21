package com.bsrdigicoin.estore1.DBproperties;

import android.content.Context;
import android.content.SharedPreferences;

import com.bsrdigicoin.estore1.recyclerView.ordersRecycerView.Orders;

import java.util.List;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String SHARED_PREFERENCE_NAME = "mysharepref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_ORDERS_LIST = "ordersList";
    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(int id, String username, String email) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
        return true;
    }

    public boolean isLoogedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        if(sharedPreferences.getString(KEY_USERNAME, null) != null)  {
            return true;
        }
        return false;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }





}
