/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;


/**
 * Application constants.
 */
public class AppConstants {
    // Define the Application Name ID as the application name without spaces or special characters.
    // So that we can define the application root folder name and the DB name prefix to be same as or
    // resemble the application name so that the users can easily identify them to belong to this application.
    private static final String APP_NAME_ID = "BhaktiVriksha";
    // Define the DB name prefix to be same as the application name without spaces or special characters.
    private static final String DB_NAME_PREFIX = APP_NAME_ID;
    public static final String BOOKS_DB_NAME = DB_NAME_PREFIX + "Library";
    public static final String BOOKMARKS_DB_NAME = DB_NAME_PREFIX + "Bookmarks";
    // Define the application root folder name to be same as the application name without spaces or special characters.
    private static final String APP_ROOT_FOLDER_PATH = "/" + APP_NAME_ID;
    public static final String BOOKS_PATH = APP_ROOT_FOLDER_PATH + "/Books";

    public static final String UTF8 = "UTF-8";

    public static final String BOOK_EXTENSION = ".bk";
    public static final String BOOK_EXTENSION_2 = ".epub";

    public static final boolean BHAKTI_VRIKSHA_APP_MODE = true;

    public static final int SPLASH_DISPLAY_TIME = 1000; // Milliseconds

    /**
     * Determines if the application running in Bhakti Vriksha app mode.
     */
    public static boolean isBhaktiVrikshaAppMode() {
        return BHAKTI_VRIKSHA_APP_MODE;
    }
}
