/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * Response data utils.
 */
public class ResponseDataUtils {

    public static boolean isUserIdVerificationTask(Response response) {
        return getBooleanValue(response, ResponseParameters.IS_USER_ID_VERIFICATION_TASK);
    }

    public static void setIsUserIdVerificationTask(Response response, boolean isUserIdVerificationTask) {
        setBooleanValue(response, ResponseParameters.IS_USER_ID_VERIFICATION_TASK, isUserIdVerificationTask);
    }

    public static boolean isMobileNumberVerificationTask(Response response) {
        return getBooleanValue(response, ResponseParameters.IS_MOBILE_NUMBER_VERIFICATION_TASK);
    }

    public static void setIsMobileNumberVerificationTask(Response response, boolean isMobileNumberVerificationTask) {
        setBooleanValue(response, ResponseParameters.IS_MOBILE_NUMBER_VERIFICATION_TASK, isMobileNumberVerificationTask);
    }

    public static boolean isTempPassword(Response response) {
        return getBooleanValue(response, ResponseParameters.IS_TEMP_PASSWORD);
    }

    public static void setIsTempPassword(Response response, boolean isTempPassword) {
        setBooleanValue(response, ResponseParameters.IS_TEMP_PASSWORD, isTempPassword);
    }

    private static boolean getBooleanValue(Response response, String parameterName) {
        Object value = (((response != null) && (parameterName != null)) ? response.getParameter(parameterName) : null);
        return (((value != null) && (value instanceof Boolean)) ? ((Boolean) value).booleanValue() : false);
    }

    private static void setBooleanValue(Response response, String parameterName, boolean value) {
        if ((response != null) && (parameterName != null)) {
            response.addParameter(parameterName, Boolean.valueOf(value));
        }
    }
}