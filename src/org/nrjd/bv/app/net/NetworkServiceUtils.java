/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.net;

public class NetworkServiceUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private NetworkServiceUtils() {
    }

    /**
     * 19/Dec/2015: Commented as this obstructs the app functionality if network not available.
     *
    public static boolean isNetworkOn(Context context) {
        boolean isNetworkOn = false;
        if (context != null) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isNetworkOn = ((networkInfo != null) && networkInfo.isConnected());
        }
        return isNetworkOn;
    }
     */
}