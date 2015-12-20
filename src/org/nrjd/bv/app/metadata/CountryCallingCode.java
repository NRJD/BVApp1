/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.metadata;


import org.nrjd.bv.app.util.StringUtils;

import java.util.Comparator;

public class CountryCallingCode implements Comparator<CountryCallingCode>, Comparable<CountryCallingCode> {
    private String callingCode = null;
    private String isoCode = null;
    private String name = null;
    private String displayString = null;
    private String isoCodeDisplayString = null;

    public CountryCallingCode(String callingCode, String isoCode, String name) {
        this.callingCode = StringUtils.trim(callingCode);
        this.isoCode = StringUtils.trim(isoCode);
        this.name = StringUtils.trim(name);
        // TODO: Localize based on the user locale.
        this.displayString = this.name + " " + this.callingCode;
        this.isoCodeDisplayString = this.isoCode + " " + this.callingCode;
    }

    /**
     * Returns the country calling code.
     *
     * @return the country calling code.
     */
    public String getCallingCode() {
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
    public int hashCode() {
        int result = (this.getCallingCode() != null) ? this.getCallingCode().hashCode() : 0;
        result = 31 * result + ((this.getISOCode() != null) ? this.getISOCode().hashCode() : 0);
        result = 31 * result + ((this.getName() != null) ? this.getName().hashCode() : 0);
        return result;
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
        if (!valueEquals(this.getCallingCode(), that.getCallingCode())) {
            return false;
        }
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
        String value1 = ((object1 != null) ? object1.getName() : null);
        String value2 = ((object2 != null) ? object2.getName() : null);
        if ((value1 == null) && (value2 == null)) {
            return 0;
        } else if (value1 == null) {
            return -1;
        } else if (value2 == null) {
            return 1;
        } else {
            return value1.compareTo(value2);
        }
    }

    @Override
    public int compareTo(CountryCallingCode another) {
        return compare(this, another);
    }

    @Override
    public String toString() {
        return this.getDisplayString();
    }

    public String toDebugString() {
        StringBuilder buffer = new StringBuilder(256);
        buffer.append(this.getISOCode()).append("=").append(this.getCallingCode()).append(" - ").append(this.getName());
        return buffer.toString();
    }
}