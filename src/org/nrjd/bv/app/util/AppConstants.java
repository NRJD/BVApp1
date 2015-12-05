/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;


/**
 * Application constants.
 */
public class AppConstants {
    public static final String APP_NAME = "BVApp1";
    public static final String BOOKS_DB_NAME = APP_NAME + "Library";
    public static final String BOOKMARKS_DB_NAME = APP_NAME + "Bookmarks";
    public static final String BOOKS_PATH = "/" + APP_NAME + "/Books";
    public static final String UTF8 = "UTF-8";

    public static final boolean BV_APP_MODE = true;

    public static final int SPLASH_DISPLAY_TIME = 1500; // Milliseconds
    public static final int STUB_DATA_SERVICE_VALIDATION_TIME = 1500; // Milliseconds

    /**
     * Determines if the application running in BV App mode.
     */
    public static boolean isBVAppMode() {
        return BV_APP_MODE;
    }
}
