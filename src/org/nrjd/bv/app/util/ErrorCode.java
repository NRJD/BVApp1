/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import org.nrjd.bv.app.R;


/**
 * Error codes used by the application.
 */
public enum ErrorCode {
    // General service errors.
    EC_SERVICE_ERROR_OCCURRED(R.string.service_error_occurred),
    EC_SERVICE_ERROR__DB_ERROR(R.string.service_error__db_error),
    EC_SERVICE_ERROR__HTTP_STATUS_CODE_ERROR(R.string.service_error__http_status_code_error),
    EC_SERVICE_ERROR__NO_NETWORK_CONNECTION(R.string.service_error__no_network_connection),
    EC_SERVICE_ERROR__COULD_NOT_CONNECT_TO_SERVICE(R.string.service_error__could_not_connect_to_service),
    EC_SERVICE_ERROR__NETWORK_TIMEOUT_DURING_READ(R.string.service_error__network_timeout_during_read),
    // Login errors.
    EC_LOGIN__EMPTY_EMAIL_ADDRESS(R.string.service_error_login__empty_email_address),
    EC_LOGIN__INVALID_EMAIL_ADDRESS(R.string.service_error_login__invalid_email_address),
    EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED(R.string.service_error_login__email_address_not_registered),
    EC_LOGIN__EMPTY_PASSWORD(R.string.service_error_login__empty_password),
    EC_LOGIN__INVALID_PASSWORD(R.string.service_error_login__invalid_password),
    EC_LOGIN__EMAIL_ADDRESS_NOT_ACTIVATED(R.string.service_error_login__email_address_not_activated),
    EC_LOGIN__MOBILE_NUMBER_NOT_VERIFIED(R.string.service_error_login__mobile_number_not_verified),
    // Registration errors.
    EC_REGISTER__EMPTY_EMAIL_ADDRESS(R.string.service_error_register__empty_email_address),
    EC_REGISTER__INVALID_EMAIL_ADDRESS(R.string.service_error_register__invalid_email_address),
    EC_REGISTER__EMAIL_ADDRESS_ALREADY_REGISTERED(R.string.service_error_register__email_address_already_registered),
    EC_REGISTER__EMAIL_ADDRESS_NOT_ACTIVATED(R.string.service_error_register__email_address_not_activated),
    EC_REGISTER__EMPTY_PASSWORD(R.string.service_error_register__empty_password),
    EC_REGISTER__EMPTY_NAME(R.string.service_error_register__empty_name),
    EC_REGISTER__EMPTY_MOBILE_COUNTRY_CODE(R.string.service_error_register__empty_mobile_country_code),
    EC_REGISTER__EMPTY_MOBILE_NUMBER(R.string.service_error_register__empty_mobile_number),
    // Reset password errors.
    EC_RESET_PASSWORD__EMPTY_EMAIL_ADDRESS(R.string.service_error_reset_password__empty_email_address),
    EC_RESET_PASSWORD__INVALID_EMAIL_ADDRESS(R.string.service_error_reset_password__invalid_email_address),
    EC_RESET_PASSWORD__EMAIL_ADDRESS_NOT_REGISTERED(R.string.service_error_reset_password__email_address_not_registered),
    EC_RESET_PASSWORD__COULD_NOT_RESET_PASSWORD(R.string.service_error_reset_password__could_not_reset_password),
    // Verify account errors.
    EC_ACTIVATE_ACCOUNT__EMPTY_EMAIL_ADDRESS(R.string.service_error_activate_account__empty_email_address),
    EC_ACTIVATE_ACCOUNT__INVALID_EMAIL_ADDRESS(R.string.service_error_activate_account__invalid_email_address),
    EC_ACTIVATE_ACCOUNT__EMAIL_ADDRESS_NOT_REGISTERED(R.string.service_error_activate_account__email_address_not_registered),
    EC_ACTIVATE_ACCOUNT__EMPTY_EMAIL_ADDRESS_VERIFICATION_CODE(R.string.service_error_activate_account__empty_email_address_verification_code),
    EC_ACTIVATE_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE(R.string.service_error_activate_account__invalid_email_address_verification_code),
    EC_ACTIVATE_ACCOUNT__INVALID_MOBILE_NUMBER(R.string.service_error_activate_account__invalid_mobile_number),
    EC_ACTIVATE_ACCOUNT__MOBILE_NUMBER_NOT_REGISTERED(R.string.service_error_activate_account__mobile_number_not_registered),
    EC_ACTIVATE_ACCOUNT__INVALID_MOBILE_NUMBER_VERIFICATION_CODE(R.string.service_error_activate_account__invalid_mobile_number_verification_code),
    EC_ACTIVATE_ACCOUNT__EMAIL_ADDRESS_ALREADY_ACTIVATED(R.string.service_error_activate_account__email_address_already_activated),
    // Resend account activation details errors.
    EC_RESEND_ACC_ACTIVATION__EMPTY_EMAIL_ADDRESS(R.string.service_error_resend_acc_activation__empty_email_address),
    EC_RESEND_ACC_ACTIVATION__INVALID_EMAIL_ADDRESS(R.string.service_error_resend_acc_activation__invalid_email_address),
    EC_RESEND_ACC_ACTIVATION__EMAIL_ADDRESS_NOT_REGISTERED(R.string.service_error_resend_acc_activation__email_address_not_registered),
    EC_RESEND_ACC_ACTIVATION__EMAIL_ADDRESS_ALREADY_ACTIVATED(R.string.service_error_resend_acc_activation__email_address_already_activated),
    EC_RESEND_ACC_ACTIVATION__COULD_NOT_RESEND_ACC_ACTIVATION_DETAILS(R.string.service_error_resend_acc_activation__could_not_resend_acc_activation_details),
    // Change Password errors.
    EC_CHG_PSWD__EMPTY_EMAIL_ADDRESS(R.string.service_error_chg_pswd__empty_email_address),
    EC_CHG_PSWD__INVALID_EMAIL_ADDRESS(R.string.service_error_chg_pswd__invalid_email_address),
    EC_CHG_PSWD__EMAIL_ADDRESS_NOT_REGISTERED(R.string.service_error_chg_pswd__email_address_not_registered),
    EC_CHG_PSWD__EMPTY_OLD_PASSWORD(R.string.service_error_chg_pswd__empty_old_password),
    EC_CHG_PSWD__INVALID_OLD_PASSWORD(R.string.service_error_chg_pswd__invalid_old_password),
    EC_CHG_PSWD__EMPTY_NEW_PASSWORD(R.string.service_error_chg_pswd__empty_new_password),
    EC_CHG_PSWD__EMAIL_ADDRESS_NOT_ACTIVATED(R.string.service_error_chg_pswd__email_address_not_activated),
    EC_CHG_PSWD__COULD_NOT_UPDATE_PASSWORD(R.string.service_error_chg_pswd__could_not_update_password);

    private int messageId = 0;

    ErrorCode(int messageId) {
        this.messageId = messageId;
    }

    public static ErrorCode getGenericErrorCode() {
        return EC_SERVICE_ERROR_OCCURRED;
    }

    public int getMessageId() {
        return this.messageId;
    }
}