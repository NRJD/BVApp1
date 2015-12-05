/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * Response data utils.
 */
public class ResponseDataUtils {
    public static final String IS_USER_ID_VERIFICATION_TASK = "IsUserIdVerificationTask";
    public static final String IS_MOBILE_NUMBER_VERIFICATION_TASK = "IsMobileNumberVerificationTask";

    public static boolean isUserIdVerificationTask(Response response) {
        Object value = ((response != null) ? response.getParameter(IS_USER_ID_VERIFICATION_TASK) : null);
        return (((value != null) && (value instanceof Boolean)) ? ((Boolean) value).booleanValue() : false);
    }

    public static boolean isMobileNumberVerificationTask(Response response) {
        Object value = ((response != null) ? response.getParameter(IS_MOBILE_NUMBER_VERIFICATION_TASK) : null);
        return (((value != null) && (value instanceof Boolean)) ? ((Boolean) value).booleanValue() : false);
    }
}