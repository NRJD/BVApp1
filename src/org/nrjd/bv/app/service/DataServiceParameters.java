/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * Response data utils.
 */
public class DataServiceParameters {
    // Data service command parameters.
    public static String CMD_LOGIN = "login";
    public static String CMD_LOG_OFF = "logoff";
    public static String CMD_REGISTER = "register";
    public static String CMD_VERIFY_ACCOUNT = "verifyFromMobile";
    public static String CMD_UPDATE_PASSWORD = "updatePassword";
    public static String CMD_RESET_PASSWORD = "resetPassword";
    public static String CMD_UPDATE_PROFILE = "updateProfile";
    public static String CMD_DOWNLOAD = "download";
    // Request parameters.
    public static String PARAM_CMD = "cmd";
    public static String PARAM_EMAIL = "email";
    public static String PARAM_PASSWORD = "password";
    public static String PARAM_NAME = "name";
    public static String PARAM_COUNTRY_CODE = "countryCode";
    public static String PARAM_PHONE_NUMBER = "phoneNum";
    public static String PARAM_LANG = "language";
    public static String PARAM_EMAIL_VERIFICATION_CODE = "vCode";
    public static String PARAM_TEMP_PASSWORD = "tempPassword";
    public static String PARAM_RESET_PASSWORD_ENABLED = "resetPwdEnabled";
    public static String PARAM_FILE_NAME = "fileName";
    // Response parameters
    public static String PARAM_STATUS_ID = "statusId";
    public static String PARAM_CODE = "code";
    public static String PARAM_MSG = "msg";
    // Status ids
    public static String STATUS_USER_ADD = "USER_ADDED";
    public static String STATUS_ACCT_NOT_VERIFIED = "ACCT_NOT_VERIFIED";
    public static String STATUS_DUPLICATE_EMAIL_ID = "DUPL_EMAILID";
}