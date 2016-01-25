/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.session;

import android.app.Activity;

import org.nrjd.bv.app.activity.ActivityUtils;


/**
 * User session utils.
 */
public class UserSessionUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private UserSessionUtils() {
    }

    public static void createLoginSession(Activity activity, UserLogin userLogin) {
        if ((activity != null) && (userLogin != null)) {
            UserSessionManager userSessionManager = getUserSessionManager(activity);
            userSessionManager.createLoginSession(userLogin);
        }
    }

    public static UserSession getUserSession(Activity activity) {
        UserSessionManager userSessionManager = getUserSessionManager(activity);
        UserSession userSession = userSessionManager.getUserSession();
        return userSession;
    }

    public static boolean isUserLoggedIn(Activity activity) {
        UserSession userSession = getUserSession(activity);
        return ((userSession != null) && userSession.isLoggedIn());
    }

    public static void cleanLoginSession(Activity activity) {
        if (activity != null) {
            UserSessionManager userSessionManager = getUserSessionManager(activity);
            userSessionManager.cleanLoginSession();
        }
    }

    private static UserSessionManager getUserSessionManager(Activity activity) {
        return SessionManagerFactory.getUserSessionManager(ActivityUtils.getContext(activity));
    }
}