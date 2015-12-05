/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import java.util.regex.Pattern;

/**
 * Pattern matching utils.
 */
public class PatternUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private PatternUtils() {
    }

    private static final Pattern EMAIL_ADDRESS_PATTERN
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean isValidEmailAddress(String emailAddress) {
        return (StringUtils.isNotNullOrEmpty(emailAddress) && (EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches()));
    }

}
