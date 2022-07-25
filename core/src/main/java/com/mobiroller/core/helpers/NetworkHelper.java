package com.mobiroller.core.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobiroller.core.R;

public class NetworkHelper {

    private Context context;
    public NetworkHelper(Context context) {
        this.context = context;
    }


    /**
     * Checking for all possible internet providers
     **/
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



    /**
     * Checking for all possible internet providers
     **/
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }

        }
        return false;
    }

    public static boolean connectionRequiredModule(String screenType,Context context) {
        String[] connectionRequiredArray = context.getResources().getStringArray(R.array.connection_required_activities);
        for (String aConnectionRequiredArray : connectionRequiredArray) {
            if (aConnectionRequiredArray.equalsIgnoreCase(screenType))
                return true;
        }
        return false;
    }
}
