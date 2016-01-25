/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import org.nrjd.bv.app.R;
import org.nrjd.bv.app.session.UserSession;
import org.nrjd.bv.app.session.UserSessionUtils;
import org.nrjd.bv.app.util.BooleanUtils;

public class MenuActionHandler {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private MenuActionHandler() {
    }

    public static void handleLogout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.logout);
        builder.setMessage(activity.getString(R.string.logout_question));
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            ActivityUtils.logoutAndGoToLoginActivity(activity);
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    public static void handleChangePassword(Activity activity) {
        Bundle loginDataParameters = getLoginDataParameters(activity);
        ActivityUtils.startChangePasswordActivity(activity, loginDataParameters);
    }

    public static void handleTechnicalSupport(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.technical_support);
        builder.setMessage(activity.getString(R.string.notes_support_email));
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private static Bundle getLoginDataParameters(Activity activity) {
        UserSession userSession = UserSessionUtils.getUserSession(activity);
        String userId = ((userSession != null) ? userSession.getUserId() : null);
        Bundle loginDataParameters = new Bundle();
        loginDataParameters.putString(ActivityParameters.USER_ID_PARAM, userId);
        loginDataParameters.putString(ActivityParameters.IS_CHANGE_TEMP_PASSWORD, BooleanUtils.getFalseValue());
        return loginDataParameters;
    }
}