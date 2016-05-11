/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.metadata;


public class NoneCountryCallingCode extends CountryCallingCode {
    private static int NONE_CALLING_CODE = CountryCallingCodeUtils.getNoneCallingCode();
    private static String NONE_ISO_COUNTRY_CODE = "00";
    private static String NONE_COUNTRY_NAME = "Select";
    // TODO: Localize this.
    private static final String localizedDisplayString = "Select Country Code";
    // TODO: Localize this.
    private static final String localizedIsoCodeDisplayString = NONE_COUNTRY_NAME;

    public NoneCountryCallingCode() {
        super(NONE_CALLING_CODE, NONE_ISO_COUNTRY_CODE, NONE_COUNTRY_NAME);
    }

    public String getDisplayString() {
        return localizedDisplayString;
    }

    public String getISOCodeDisplayString() {
        return localizedIsoCodeDisplayString;
    }
}