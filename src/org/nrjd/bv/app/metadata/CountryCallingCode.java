/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.metadata;


import org.nrjd.bv.app.util.CommonUtils;
import org.nrjd.bv.app.util.StringUtils;

import java.util.Comparator;

public class CountryCallingCode implements Comparator<CountryCallingCode>, Comparable<CountryCallingCode> {
    private int callingCode = CountryCallingCodeUtils.getNoneCallingCode();
    private String isoCode = null;
    private String name = null;
    private String displayString = null;
    private String isoCodeDisplayString = null;

    public CountryCallingCode(int callingCode, String isoCode, String name) {
        this.callingCode = (CountryCallingCodeUtils.isValidCountryCallingCode(callingCode) ? callingCode : CountryCallingCodeUtils.getNoneCallingCode());
        this.isoCode = isoCode;
        this.name = StringUtils.trim(name);
        // BVApp-Comment: 21/Feb2/2016: TODO: For now showing isoCode for the display string.
        // But this needs tobe enhanced to use the calling code name.
        // Also localize the calling code names.
        //// this.displayString = this.name + " " + this.callingCode;
        this.displayString = this.isoCode + " " + CountryCallingCodeUtils.getFormattedCallingCode(this.callingCode, true);
        this.isoCodeDisplayString = this.isoCode + " " + CountryCallingCodeUtils.getFormattedCallingCode(this.callingCode, false);
    }

    /**
     * Returns the country calling code.
     *
     * @return the country calling code.
     */
    public int getCallingCode() {
        return this.callingCode;
    }

    /**
     * Returns the country ISO code (2 letter code).
     *
     * @return the country ISO code (2 letter code).
     */
    public String getISOCode() {
        return this.isoCode;
    }

    /**
     * Returns the country name.
     *
     * @return the country name.
     */
    public String getName() {
        return this.name;
    }

    public String getDisplayString() {
        return this.displayString;
    }

    public String getISOCodeDisplayString() {
        return this.isoCodeDisplayString;
    }

    @Override
    public String toString() {
        return this.getDisplayString();
    }

    @Override
    public int hashCode() {
        return this.getCallingCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        CountryCallingCode that = (CountryCallingCode) object;
        // Note: We shouldn't compare calling code, because the calling code
        // might be same for two or more countries.
        /*
        if (this.getCallingCode() != that.getCallingCode()) {
            return false;
        }
        */
        if (!valueEquals(this.getISOCode(), that.getISOCode())) {
            return false;
        }
        if (!valueEquals(this.getName(), that.getName())) {
            return false;
        }
        return true;
    }

    private boolean valueEquals(String value1, String value2) {
        if ((value1 == null) && (value2 == null)) {
            return true;
        } else if (value1 == null) {
            return false;
        } else if (value2 == null) {
            return false;
        } else {
            return value1.equals(value2);
        }
    }

    @Override
    public int compare(CountryCallingCode object1, CountryCallingCode object2) {
        // BVApp-Comment: 21/Feb2/2016: TODO: Currently sorting by calling code and ISO code value.
        // Once we fix to show the calling code names in the drop down options,
        // then we have to change this to sort by calling code name values.
        //// return compareNameValue(object1, object2);
        int value = compareCallingCodeValue(object1, object2);
        if(value == 0) {
            value = compareIsoCodeValue(object1, object2);
        }
        return value;
    }

    private static int compareCallingCodeValue(CountryCallingCode object1, CountryCallingCode object2) {
        int value1 = ((object1 != null) ? object1.getCallingCode() : CountryCallingCodeUtils.getNoneCallingCode());
        int value2 = ((object2 != null) ? object2.getCallingCode() : CountryCallingCodeUtils.getNoneCallingCode());
        return CommonUtils.compareIntValue(value1, value2);
    }

    private static int compareIsoCodeValue(CountryCallingCode object1, CountryCallingCode object2) {
        String value1 = ((object1 != null) ? object1.getISOCode() : null);
        String value2 = ((object2 != null) ? object2.getISOCode() : null);
        return CommonUtils.compareStringValue(value1, value2);
    }

    private static int compareNameValue(CountryCallingCode object1, CountryCallingCode object2) {
        String value1 = ((object1 != null) ? object1.getName() : null);
        String value2 = ((object2 != null) ? object2.getName() : null);
        return CommonUtils.compareStringValue(value1, value2);
    }

    @Override
    public int compareTo(CountryCallingCode another) {
        return compare(this, another);
    }

    public String toDebugString() {
        StringBuilder buffer = new StringBuilder(256);
        buffer.append(this.getISOCode()).append("=").append(this.getCallingCode()).append(" - ").append(this.getName());
        return buffer.toString();
    }
}