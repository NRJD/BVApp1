/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User session manager.
 */
public class UserSessionManager extends BaseSessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionManager.class);

    // Shared preferences file name.
    private static final String PREFERENCES_FILE_NAME = "UserSession";
    // Shared preferences keys
    public static final String USER_ID = "userId";
    public static final String PSWD = "pswd";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String LAST_LOGIN = "lastLogin";

    public UserSessionManager(Context context) {
        super(context, PREFERENCES_FILE_NAME);
    }

    public UserSession createLoginSession(UserLogin userLogin) {
        // Clean the existing login session.
        cleanLoginSession();
        // Create session.
        UserSession userSession = getUserSession();
        updateLoginInfo(userSession, userLogin);
        if (userSession.isLoggedIn()) {
            Editor editor = getPreferencesEditor();
            editor.putString(USER_ID, userSession.getUserId());
            editor.putString(PSWD, userSession.getPassword());
            editor.putBoolean(IS_LOGGED_IN, userSession.isLoggedIn());
            editor.putString(LAST_LOGIN, userSession.getLastLogin());
            editor.commit();
        }
        return userSession;
    }

    public void cleanLoginSession() {
        Editor editor = getPreferencesEditor();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.remove(USER_ID);
        editor.remove(PSWD);
        // Retain the last login info.
        editor.commit();
    }

    public UserSession getUserSession() {
        SharedPreferences prefs = getPreferences();
        UserLogin userLogin = null;
        String userId = prefs.getString(USER_ID, null);
        String password = prefs.getString(PSWD, null);
        boolean isLoggedIn = prefs.getBoolean(IS_LOGGED_IN, false);
        String lastLogin = prefs.getString(LAST_LOGIN, null);
        // Process user login info.
        if (isLoggedIn && StringUtils.isNotNullOrEmpty(userId) && StringUtils.isNotNullOrEmpty(password)) {
            isLoggedIn = true;
            userLogin = new UserLogin(userId, password);
        } else {
            isLoggedIn = false;
            userLogin = null;
        }
        // Populate into user session object.
        UserSession userSession = new UserSession();
        userSession.setUserLogin(userLogin);
        userSession.setIsLoggedIn(isLoggedIn);
        userSession.setLastLogin(lastLogin);
        return userSession;
    }

    private static void updateLoginInfo(UserSession userSession, UserLogin userLogin) {
        if ((userSession != null) && (userLogin != null)) {
            String userId = userLogin.getUserId();
            String password = userLogin.getPassword();
            if (StringUtils.isNotNullOrEmpty(userId) && StringUtils.isNotNullOrEmpty(password)) {
                userSession.setUserLogin(userLogin);
                userSession.setIsLoggedIn(true);
                userSession.setLastLogin(userId);
            }
        }
    }

    private static void updateLogoutInfo(UserSession userSession) {
        if (userSession != null) {
            userSession.setUserLogin(null);
            userSession.setIsLoggedIn(false);
            // Retain the last login info.
        }
    }
}