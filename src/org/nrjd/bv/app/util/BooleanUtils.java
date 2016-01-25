/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;


public class BooleanUtils {
    private static final String VALUE_TRUE = "true";
    private static final String VALUE_FALSE = "false";
    private static final String VALUE_YES = "yes";
    private static final String VALUE_Y = "y";
    private static final String VALUE_1 = "1";

    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private BooleanUtils() {
    }

    public static String getTrueValue() {
        return VALUE_TRUE;
    }

    public static String getFalseValue() {
        return VALUE_FALSE;
    }

    public static boolean isTrue(String string) {
        if (string != null) {
            return (VALUE_TRUE.equalsIgnoreCase(string) || VALUE_YES.equalsIgnoreCase(string) ||
                    VALUE_Y.equalsIgnoreCase(string) || VALUE_1.equalsIgnoreCase(string));
        }
        return false;
    }
}
