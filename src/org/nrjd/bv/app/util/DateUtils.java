/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat[] DATE_FORMAT_LIST =
            new DateFormat[]{DEFAULT_DATE_FORMAT, new SimpleDateFormat("yyyy-MM-dd HH:mm"), new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy/MM/dd"), new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")};

    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private DateUtils() {
    }

    /**
     * Parses the given date string representation and returns the corresponding date value.
     *
     * @param dateString date string to be parsed for the date value.
     * @return the equivalent date value parsed from the given date string.
     * @throws Exception if any error occurs while parsing the date string.
     */
    public static Date parseDate(String dateString) {
        Date date = null;
        if (dateString != null) {
            for (DateFormat auditTimestampFormat : DATE_FORMAT_LIST) {
                try {
                    date = auditTimestampFormat.parse(dateString);
                    if (date != null) {
                        break; // If the date string is parsed successfully, then break from here.
                    }
                } catch (Exception e) {
                    date = null; // Ignore the exception.
                }
            }
        }
        return date;
    }

    /**
     * Formats the given date value into the string format.
     *
     * @param date date value to be formatted.
     * @return the string format of the given date value.
     * @throws Exception if any error occurs while formatting the date value.
     */
    public static String formatDate(Date date) {
        return ((date != null) ? DEFAULT_DATE_FORMAT.format(date) : null);
    }
}
