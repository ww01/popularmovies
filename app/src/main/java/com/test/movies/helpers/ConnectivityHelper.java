package com.test.movies.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.test.popularmovies.R;

/**
 * Created by waldek on 03.04.17.
 */

public class ConnectivityHelper {

    private ConnectivityHelper(){}

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isNetworkAvailableMsg(Context context){
        boolean result = ConnectivityHelper.isNetworkAvailable(context);
        if(!result)
            Toast.makeText(context, context.getText(R.string.no_network), Toast.LENGTH_LONG).show();

        return result;
    }
}
