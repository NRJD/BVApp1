/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.R;


/**
 * Service error codes.
 */
public enum ErrorCode {
    // General errors.
    EC_SERVICE_ERROR_OCCURRED(-2001, R.string.service_error_occurred),
    // Login errors.
    EC_LOGIN__EMPTY_EMAIL_ADDRESS(-2101, R.string.service_error_login__empty_email_address),
    EC_LOGIN__INVALID_EMAIL_ADDRESS(-2102, R.string.service_error_login__invalid_email_address),
    EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED(-2103, R.string.service_error_login__email_address_not_registered),
    EC_LOGIN__EMPTY_PASSWORD(-2104, R.string.service_error_login__empty_password),
    EC_LOGIN__INVALID_PASSWORD(-2105, R.string.service_error_login__invalid_password),
    EC_LOGIN__EMAIL_ADDRESS_NOT_VERIFIED(-2106, R.string.service_error_login__email_address_not_verified),
    EC_LOGIN__MOBILE_NUMBER_NOT_VERIFIED(-2107, R.string.service_error_login__mobile_number_not_verified),
    // Registration errors.
    EC_REGISTER__EMPTY_EMAIL_ADDRESS(-2201, R.string.service_error_register__empty_email_address),
    EC_REGISTER__INVALID_EMAIL_ADDRESS(-2202, R.string.service_error_register__invalid_email_address),
    EC_REGISTER__EMAIL_ADDRESS_ALREADY_REGISTERED(-2203, R.string.service_error_register__email_address_already_registered),
    EC_REGISTER__EMPTY_PASSWORD(-2204, R.string.service_error_register__empty_password),
    EC_REGISTER__EMPTY_NAME(-2205, R.string.service_error_register__empty_name),
    EC_REGISTER__EMPTY_MOBILE_COUNTRY_CODE(-2206, R.string.service_error_register__empty_mobile_country_code),
    EC_REGISTER__EMPTY_MOBILE_NUMBER(-2207, R.string.service_error_register__empty_mobile_number),
    // Reset password errors.
    EC_RESET_PASSWORD__EMPTY_EMAIL_ADDRESS(-2301, R.string.service_error_reset_password__empty_email_address),
    EC_RESET_PASSWORD__INVALID_EMAIL_ADDRESS(-2302, R.string.service_error_reset_password__invalid_email_address),
    EC_RESET_PASSWORD__EMAIL_ADDRESS_NOT_REGISTERED(-2303, R.string.service_error_reset_password__email_address_not_registered),
    // Verify account errors.
    EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS(-2401, R.string.service_error_verify_account__invalid_email_address),
    EC_VERIFY_ACCOUNT__EMAIL_ADDRESS_NOT_REGISTERED(-2402, R.string.service_error_verify_account__email_address_not_registered),
    EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE(-2403, R.string.service_error_verify_account__invalid_email_address_verification_code),
    EC_VERIFY_ACCOUNT__INVALID_MOBILE_NUMBER(-2404, R.string.service_error_verify_account__invalid_mobile_number),
    EC_VERIFY_ACCOUNT__MOBILE_NUMBER_NOT_REGISTERED(-2405, R.string.service_error_verify_account__mobile_number_not_registered),
    EC_VERIFY_ACCOUNT__INVALID_MOBILE_NUMBER_VERIFICATION_CODE(-2406, R.string.service_error_verify_account__invalid_mobile_number_verification_code),
    // Change Password errors.
    EC_CHG_PSWD__EMPTY_EMAIL_ADDRESS(-2501, R.string.service_error_chg_pswd__empty_email_address),
    EC_CHG_PSWD__INVALID_EMAIL_ADDRESS(-2502, R.string.service_error_chg_pswd__invalid_email_address),
    EC_CHG_PSWD__EMAIL_ADDRESS_NOT_REGISTERED(-2503, R.string.service_error_chg_pswd__email_address_not_registered),
    EC_CHG_PSWD__EMPTY_OLD_PASSWORD(-2504, R.string.service_error_chg_pswd__empty_old_password),
    EC_CHG_PSWD__INVALID_OLD_PASSWORD(-2505, R.string.service_error_chg_pswd__invalid_old_password),
    EC_CHG_PSWD__EMPTY_NEW_PASSWORD(-2507, R.string.service_error_chg_pswd__empty_new_password),
    EC_CHG_PSWD__EMAIL_ADDRESS_NOT_VERIFIED(-2506, R.string.service_error_chg_pswd__email_address_not_verified);

    private int serviceErrorCode = 0;
    private int messageId = 0;

    ErrorCode(int serviceErrorCode, int messageId) {
        this.serviceErrorCode = serviceErrorCode;
        this.messageId = messageId;
    }

    public static ErrorCode getGenericErrorCode() {
        return EC_SERVICE_ERROR_OCCURRED;
    }

    public int getMessageId() {
        return this.messageId;
    }
}