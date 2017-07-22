package com.bsi.presensidosen.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.bsi.presensidosen.LoginActivity;

import java.util.HashMap;

/**
 * Created by Mukhlasin on 9/6/2016.
 */
public class SessionManagement {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    private final String TAG = LoginActivity.class.getSimpleName();

    // Sharedpref file name
    private static final String PREF_NAME = "PresensiDosen";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "Id_Pegawai";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_NIK = "Nik";
    public static final String KEY_NAMA_LENGKAP = "Nama";
    public static final String KEY_NAMA_PRODI = "Nama_Prodi";
    public static final String KEY_PHOTOS = "Photos";

    public static final String KEY_IDPRES = "Id_Presensi";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        Log.d(TAG, "tess 0");
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String Id_Pegawai, String Email, String Nik, String Nama, String Nama_Prodi, String Photos){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, Id_Pegawai);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_NIK, Nik);
        editor.putString(KEY_NAMA_LENGKAP, Nama);
        editor.putString(KEY_NAMA_PRODI, Nama_Prodi);
        editor.putString(KEY_PHOTOS, Photos);

        // commit or apply changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

            user.put(KEY_ID, pref.getString(KEY_ID, null));
            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
            user.put(KEY_NIK, pref.getString(KEY_NIK, null));
            user.put(KEY_NAMA_LENGKAP, pref.getString(KEY_NAMA_LENGKAP, null));
            user.put(KEY_NAMA_PRODI, pref.getString(KEY_NAMA_PRODI, null));
            user.put(KEY_PHOTOS, pref.getString(KEY_PHOTOS, null));

            

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities

        //for close login activity
        i.putExtra("EXIT", true);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    public void sudahAbsen(){
        editor.putString("ABSEN", "yes");
        editor.commit();

    }

    public void hapusAbsen(){
        editor.remove("ABSEN");
        editor.commit();

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void getAddLastPresensiID(String Id_Presensi){
        // Storing login value as TRUE
       editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_IDPRES, Id_Presensi);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getDataLastPresensi(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_IDPRES, pref.getString(KEY_IDPRES, null));
        // return user
        return user;
    }

}
