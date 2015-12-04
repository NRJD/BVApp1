/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

import net.nightwhistler.pageturner.R;

public enum ErrorCode {
    EC_SERVICE_ERROR_OCCURRED(-1, R.string.service_error_occured),
    EC_LOGIN__INVALID_EMAIL_ADDRESS(-2001, R.string.service_error_login__invalid_email_address),
    EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED(-2002, R.string.service_error_login__email_address_not_registered),
    EC_LOGIN__INVALID_PASSWORD(-2003, R.string.service_error_login__invalid_password),
    EC_LOGIN__EMAIL_ADDRESS_NOT_VERIFIED(-2004, R.string.service_error_login__email_not_verified),
    EC_LOGIN__MOBILE_NUMBER_NOT_VERIFIED(-2005, R.string.service_error_login__mobile_number_not_verified);

    private int serviceErrorCode = 0;
    private int messageId = 0;

    ErrorCode(int serviceErrorCode, int messageId) {
        this.serviceErrorCode = serviceErrorCode;
        this.messageId = messageId;
    }

    public static ErrorCode getGenericErrorCode() {
        return EC_SERVICE_ERROR_OCCURRED;
    }

    public int getServiceErrorCode() {
        return this.serviceErrorCode;
    }

    public int getMessageId() {
        return this.messageId;
    }
}