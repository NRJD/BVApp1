/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.nrjd.bv.app.ctx.AppContext;

public class NetworkServiceUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private NetworkServiceUtils() {
    }

    public static boolean isNetworkOn(AppContext appContext) {
        return isNetworkOn((appContext != null) ? appContext.getContext() : null);
    }

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
}