/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


public class ViewUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private ViewUtils() {
    }

    public static void removeView(View view) {
        if (view != null) {
            ViewParent parentView = view.getParent();
            if ((parentView != null) && (parentView instanceof ViewGroup)) {
                ((ViewGroup) parentView).removeView(view);
            }
        }
    }
}