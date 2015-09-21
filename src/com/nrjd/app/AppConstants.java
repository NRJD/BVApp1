package com.nrjd.app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final String NET_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";
    private static final List<String> BOOK_NAMES = Arrays.<String>asList("A1.epub", "G1.epub", "M1.epub");

    /**
     * Determines if the application running in BV App mode.
     */
    public static boolean isBVAppMode() {
        return BV_APP_MODE;
    }

    public static List<String> getBookNames() {
        return BOOK_NAMES;
    }

    public static String getBookUrl(String bookName) {
        return NET_FOLDER + "/" + bookName;
    }

    public static List<String> getBookUrls() {
        List<String> bookUrls = new ArrayList<>();
        for (String bookName : BOOK_NAMES) {
            bookUrls.add(getBookUrl(bookName));
        }
        return bookUrls;
    }
}
