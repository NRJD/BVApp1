/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;


public class SystemUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private SystemUtils() {
    }

    /**
     * Returns screen width in pixels.
     * If couldn't retrieve, then it returns -1.
     */
    public static int getScreenWidthPixels(Activity activity) {
        DisplayMetrics displayMetrics = SystemServiceHelper.getDefaultDisplayMetrics(activity);
        return ((displayMetrics != null) ? displayMetrics.widthPixels : -1);
    }

    /**
     * Returns screen height in pixels.
     * If couldn't retrieve, then it returns -1.
     */
    public static int getScreenHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = SystemServiceHelper.getDefaultDisplayMetrics(activity);
        return ((displayMetrics != null) ? displayMetrics.heightPixels : -1);
    }

    public static void dismissKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = SystemServiceHelper.getInputMethodManager(activity);
        // If the soft keyboard is open, then dismiss it.
        if ((activity != null) && (inputMethodManager != null) && inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}