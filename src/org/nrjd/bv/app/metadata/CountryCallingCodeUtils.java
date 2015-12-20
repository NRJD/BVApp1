/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.metadata;


import org.nrjd.bv.app.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CountryCallingCodeUtils {
    // TODO: Localize based on the user locale. Reload if user changes the locale.
    private static final List<CountryCallingCode> callingCodes = initializeCallingCodes();

    public static List<CountryCallingCode> getCountryCallingCodes() {
        return callingCodes;
    }

    public static int getSelectedIndex(String isoCountryCode) {
        int selectedPosition = -1;
        if(StringUtils.isNotNullOrEmpty(isoCountryCode)) {
            isoCountryCode = isoCountryCode.trim();
            for(int index=0; index < callingCodes.size(); index++) {
                if(isoCountryCode.equalsIgnoreCase(callingCodes.get(index).getISOCode())) {
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
        orderedCallingCodes.add(new CountryCallingCode("+91", "IN", "India"));
        orderedCallingCodes.add(new CountryCallingCode("+1", "US", "United States of America"));
        orderedCallingCodes.add(new CountryCallingCode("+44", "UK", "United Kingdom"));
        orderedCallingCodes.add(new CountryCallingCode("+86", "CN", "China"));
        orderedCallingCodes.add(new CountryCallingCode("+801", "81", "Temp country calling code with the medium text"));
        orderedCallingCodes.add(new CountryCallingCode("+802", "82", "Temp country calling code with a longer text for the country name value"));
        for (int index = 0; index < 50; index++) {
            char ch1 = ((index < 25) ? 'Y' : 'Z');
            char ch2 = (char) (65 + (index % 25));
            String tempCountryCode = "" + ch1 + ch2;
            orderedCallingCodes.add(new CountryCallingCode("+9" + index, tempCountryCode, "Temp country " + tempCountryCode));
        }
        return orderedCallingCodes;
    }
}