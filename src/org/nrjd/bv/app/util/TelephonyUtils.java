/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import android.app.Activity;
import android.telephony.TelephonyManager;


public class TelephonyUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private TelephonyUtils() {
    }

    public static String getSimCountryIso(Activity activity) {
        TelephonyManager telephonyManager = SystemServiceHelper.getTelephonyManager(activity);
        String simCountryIso = ((telephonyManager != null) ? telephonyManager.getSimCountryIso() : null);
        return simCountryIso;
    }

    public static String getNetworkCountryIso(Activity activity) {
        TelephonyManager telephonyManager = SystemServiceHelper.getTelephonyManager(activity);
        String networkCountryIso = ((telephonyManager != null) ? telephonyManager.getNetworkCountryIso() : null);
        return networkCountryIso;
    }
}