/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import org.nrjd.bv.app.activity.ActivityUtils;


public class SystemServiceHelper {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private SystemServiceHelper() {
    }

    public static DisplayMetrics getDefaultDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = null;
        if (activity != null) {
            displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    public static InputMethodManager getInputMethodManager(Activity activity) {
        InputMethodManager inputMethodManager = null;
        Context context = ActivityUtils.getContext(activity);
        if (context != null) {
            inputMethodManager = (InputMethodManager) activity.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return inputMethodManager;
    }

    public static TelephonyManager getTelephonyManager(Activity activity) {
        TelephonyManager telephonyManager = null;
        Context context = ActivityUtils.getContext(activity);
        if (context != null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return telephonyManager;
    }
}