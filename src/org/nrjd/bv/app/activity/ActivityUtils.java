/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.nightwhistler.pageturner.activity.ReadingActivity;

import java.util.Map;
import java.util.Set;

public class ActivityUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private ActivityUtils() {
    }

    public static void startLoginActivity(Activity sourceActivity) {
        startActivity(sourceActivity, LoginActivity.class);
    }

    public static void startRegisterActivity(Activity sourceActivity) {
        startActivity(sourceActivity, RegisterActivity.class);
    }

    public static void startResetPasswordActivity(Activity sourceActivity) {
        startActivity(sourceActivity, ResetPasswordActivity.class);
    }

    public static void startReadingActivity(Activity sourceActivity) {
        startActivity(sourceActivity, ReadingActivity.class);
    }

    public static void startActivity(Activity sourceActivity, Class<?> targetActivityClass) {
        startActivity(sourceActivity, targetActivityClass, null);
    }

    public static void startActivity(Activity sourceActivity, Class<?> targetActivityClass, Map<String, Bundle> parameters) {
        Intent intent = new Intent(sourceActivity.getApplicationContext(), targetActivityClass);
        populateParameters(intent, parameters);
        sourceActivity.startActivity(intent);
    }

    private static void populateParameters(Intent intent, Map<String, Bundle> parameters) {
        if ((intent != null) && (parameters != null)) {
            Set<String> parameterNames = parameters.keySet();
            if (parameterNames != null) {
                for (String parameterName : parameterNames) {
                    Bundle bundle = parameters.get(parameterName);
                    if (bundle != null) {
                        intent.putExtra(parameterName, parameters.get(parameterName));
                    }
                }
            }
        }
    }
}