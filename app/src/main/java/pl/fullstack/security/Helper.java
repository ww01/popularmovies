package pl.fullstack.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by waldek on 16.07.17.
 */

public class Helper {

    public static final String PREF_KEY = "LOGIN_TOKEN";

    private Helper(){}

    public static String SHA256 (String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static boolean isLoggedIn(Context context){

        SharedPreferences preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_KEY)){
            String loginToken = preferences.getString(PREF_KEY, null);
            return loginToken != null && loginToken.length() > 0;
        }

        return false;
    }

    public static boolean setupLoginToken(Context context, String token){
        SharedPreferences preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        return preferences.edit().putString(PREF_KEY, token).commit();
    }

    public static boolean logout(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        return preferences.edit().remove(PREF_KEY).commit();
    }

}
