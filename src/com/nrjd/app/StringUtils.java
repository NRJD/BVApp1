/*
 * Copyright (C) 2013 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */

package com.nrjd.app;

import java.util.List;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return ((str == null) || str.trim().equals(""));
    }

    public static String toString(List<String> stringValues, String delimiter, boolean appendIndex) {
        if (stringValues == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder(256);
        toString(buffer, stringValues, delimiter, appendIndex);
        return buffer.toString();
    }

    public static void toString(StringBuilder buffer, List<String> stringValues, String delimiter, boolean appendIndex) {
        if (buffer == null) {
            return;
        }
        if (stringValues != null) {
            buffer.append("[size=");
            buffer.append(stringValues.size());
            if (stringValues.size() > 0) {
                int index = 1;
                if (delimiter == null) {
                    delimiter = "";
                }
                for (String stringValue : stringValues) {
                    buffer.append(delimiter);
                    if (appendIndex) {
                        buffer.append(index++).append(": ");
                    }
                    buffer.append(stringValue);
                }
            }
            buffer.append("]");
        } else {
            buffer.append("null");
        }
    }
}
