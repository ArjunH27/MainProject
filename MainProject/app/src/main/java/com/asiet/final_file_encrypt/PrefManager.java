package com.asiet.final_file_encrypt;

/**
 * Created by Akash Bhaskaran on 13-07-2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref,key,enckey,phone,incorrect;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String PREF_KEY = "key";
    private static final String PREF_ENCKEY = "enckey";
    private static final String PREF_PHONE = "phone";
    private static final String PREF_INCORRECT = "incorrect";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        key = _context.getSharedPreferences(PREF_KEY, PRIVATE_MODE);
        enckey = _context.getSharedPreferences(PREF_ENCKEY, PRIVATE_MODE);
        phone = _context.getSharedPreferences(PREF_PHONE, PRIVATE_MODE);
        incorrect = _context.getSharedPreferences(PREF_INCORRECT, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setincorrectpass(int value) {
        incorrect = _context.getSharedPreferences(PREF_INCORRECT, PRIVATE_MODE);
        editor = incorrect.edit();
        editor.putInt("incorrect", value);
        editor.commit();
    }
    public int getincorrectpass() {
       return incorrect.getInt("incorrect",0);
    }

    public void setkey(int k) {
        key = _context.getSharedPreferences(PREF_KEY, PRIVATE_MODE);
        editor = key.edit();
        editor.putInt("key1", k);
        editor.commit();

    }
    public void setenckey(byte[] k) {
        enckey = _context.getSharedPreferences(PREF_ENCKEY, PRIVATE_MODE);
        editor = enckey.edit();

        String savethis = Base64.encodeToString(k,Base64.DEFAULT);
        editor.putString("enckey", savethis);
        editor.commit();

    }
    public void setPhone(long num) {
        phone = _context.getSharedPreferences(PREF_PHONE, PRIVATE_MODE);
        editor = phone.edit();
        editor.putLong("phone", num);
        editor.commit();

    }

    public Integer get_key() {
        return key.getInt("key1", 0);
    }
    public Long get_phone() {

        return phone.getLong("phone", 0);
    }
    public String get_enckey() {
        return enckey.getString("enckey",null);
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}