/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.nightwhistler.pageturner.activity.ReadingActivity;

import org.nrjd.bv.app.ctx.AppContext;
import org.nrjd.bv.app.session.UserSessionUtils;

public class ActivityUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private ActivityUtils() {
    }

    public static Context getContext(Activity activity) {
        return ((activity != null)? activity.getBaseContext() : null);
    }

    public static AppContext getAppContext(Activity activity) {
        return new AppContext(getContext(activity));
    }

    /**
     * Clear the user login session, clears the activity history and redirect to login screen.
     */
    public static void logoutAndGoToLoginActivity(Activity sourceActivity) {
        // Clear the user login session.
        UserSessionUtils.cleanLoginSession(sourceActivity);
        // Clear the activity history and start the login activity.
        int[] flagsToClearActivityHistory = new int[]{Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK};
        startLoginActivity(sourceActivity, flagsToClearActivityHistory);
    }

    public static void startLoginActivity(Activity sourceActivity) {
        startLoginActivity(sourceActivity, null);
    }

    public static void startLoginActivity(Activity sourceActivity, int[] flags) {
        startActivity(sourceActivity, LoginActivity.class, null, flags);
    }

    public static void startRegisterActivity(Activity sourceActivity) {
        startActivity(sourceActivity, RegisterActivity.class);
    }

    public static void startResetPasswordActivity(Activity sourceActivity) {
        startActivity(sourceActivity, ResetPasswordActivity.class);
    }

    public static void startVerifyAccountActivity(Activity sourceActivity, Bundle parameters) {
        startActivity(sourceActivity, VerifyAccountActivity.class, parameters, null);
    }

    public static void startResendAccountActivationActivity(Activity sourceActivity, Bundle parameters) {
        startActivity(sourceActivity, ResendAccountActivationActivity.class, parameters, null);
    }

    public static void startChangePasswordActivity(Activity sourceActivity, Bundle parameters) {
        startActivity(sourceActivity, ChangePasswordActivity.class, parameters, null);
    }

    public static void startReadingActivity(Activity sourceActivity) {
        startActivity(sourceActivity, ReadingActivity.class);
    }

    public static void startActivity(Activity sourceActivity, Class<?> targetActivityClass) {
        startActivity(sourceActivity, targetActivityClass, null, null);
    }

    public static void startActivity(Activity sourceActivity, Class<?> targetActivityClass, Bundle parameters, int[] flags) {
        Intent intent = new Intent(sourceActivity.getApplicationContext(), targetActivityClass);
        if(parameters != null) {
            intent.putExtras(parameters);
        }
        if(flags != null) {
            for(int flag : flags) {
                intent.addFlags(flag);
            }
        }
        sourceActivity.startActivity(intent);
        handleSourceActivityEvents(sourceActivity);
    }

    private static void handleSourceActivityEvents(Activity sourceActivity) {
        if (sourceActivity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) sourceActivity;
            if (!baseActivity.retainActivityInBackButtonHistory()) {
                // The user is leaving this activity at this point. Destroying this activity so that
                // this activity will not be shown to the user when the user presses the back button.
                baseActivity.finish();
            }
        }
    }
}