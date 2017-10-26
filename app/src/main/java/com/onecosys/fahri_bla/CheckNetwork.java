package com.onecosys.fahri_bla;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by User on 26.10.2017.
 */

public class CheckNetwork {
    
    private static final String LOG_TAG = CheckNetwork.class.getSimpleName();
    
    public static boolean isInternetAvailable(Context context) {

        NetworkInfo info = (NetworkInfo)
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            Log.d(LOG_TAG, "no internet connection");
            return false;
        } else {
            if (info.isConnected()) {
                Log.d(LOG_TAG, " internet connection available...");
                return true;
            } else {
                Log.d(LOG_TAG, " internet connection");
                return true;
            }

        }
    }
}//end of class CheckNetwork
