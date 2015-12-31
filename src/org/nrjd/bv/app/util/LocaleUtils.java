/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import java.util.Locale;


public class LocaleUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private LocaleUtils() {
    }

    public static Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public static String getDefaultLocaleCountryCode() {
        return getDefaultLocale().getCountry();
    }
}