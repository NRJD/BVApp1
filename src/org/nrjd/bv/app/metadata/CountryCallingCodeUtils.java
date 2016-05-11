/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.metadata;


import org.nrjd.bv.app.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CountryCallingCodeUtils {
    private static final String BLANK = "";
    private static final String SPACE = " ";
    private static final String PLUS = "+";
    private static final String CALLING_CODE_SPACING_FOR_1_DIGIT = SPACE + SPACE;
    private static final String CALLING_CODE_SPACING_FOR_2_DIGITS = SPACE;

    // TODO: Localize based on the user locale. Reload if user changes the locale.
    private static final List<CountryCallingCode> callingCodes = initializeCallingCodes();

    public static List<CountryCallingCode> getCountryCallingCodes() {
        return callingCodes;
    }

    public static final int getNoneCallingCode() {
        return 0;
    }

    public static boolean isValidCountryCallingCode(int callingCode) {
        return ((callingCode >= 1) && (callingCode <= 999));
    }

    public static String getFormattedCallingCode(int callingCode, boolean addSpaces) {
        String spacing = BLANK;
        if (callingCode <= 9) {
            spacing = CALLING_CODE_SPACING_FOR_1_DIGIT;
        } else if (callingCode <= 99) {
            spacing = CALLING_CODE_SPACING_FOR_2_DIGITS;
        }
        String formattedString = (addSpaces? spacing : BLANK) + PLUS + callingCode;
        return formattedString;
    }

    public static int getSelectedIndex(String isoCountryCode) {
        int selectedPosition = -1;
        if (StringUtils.isNotNullOrEmpty(isoCountryCode)) {
            isoCountryCode = isoCountryCode.trim();
            for (int index = 0; index < callingCodes.size(); index++) {
                if (isoCountryCode.equalsIgnoreCase(callingCodes.get(index).getISOCode())) {
                    return index;
                }
            }
        }
        return selectedPosition;
    }

    private static List<CountryCallingCode> initializeCallingCodes() {
        List<CountryCallingCode> callingCodesList = new ArrayList<CountryCallingCode>();
        callingCodesList.add(new NoneCountryCallingCode());
        callingCodesList.addAll(populateOrderedCallingCodes());
        return Collections.unmodifiableList(callingCodesList);
    }

    private static Set<CountryCallingCode> populateOrderedCallingCodes() {
        Set<CountryCallingCode> orderedCallingCodes = new TreeSet<CountryCallingCode>();
        for (CountryCallingCode countryCallingCode : CountryCallingCodeList.CALLING_CODES) {
            orderedCallingCodes.add(countryCallingCode);
        }
        return orderedCallingCodes;
    }
}