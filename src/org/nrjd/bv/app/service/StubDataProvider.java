/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class StubDataProvider {
    private static final String EMAIL_SEPARATOR = "@";
    private static final String[] VALID_DOMAINS = new String[]{"abc.com", "a.a", "a.b", "a.com", "b.com", "c.com", "nrjd.com"};
    private static final StubDataProvider INSTANCE = new StubDataProvider();
    private static Map<String, String> VALID_USER_IDS = generatePasswordMap(new String[]{"u1", "u2", "u3", "u4", "u5", "u6", "u7", "u8", "u9", "u10"});
    private static Set<String> VERIFIED_EMAIL_ADDRESSES = generateEmailAddressSet(new String[]{"u1", "u2", "u3", "u4", "u5"});
    private static Set<String> VERIFIED_MOBILE_NUMBERS = generateEmailAddressSet(new String[]{"u1", "u2", "u3"});
    private static List<String> MOBILE_NUMBERS = Arrays.<String>asList("1234567891", "1234567892", "1234567893");
    private static List<String> EMAIL_VERIFICATION_CODES = Arrays.<String>asList("123451", "123452", "123453");
    private static List<String> MOBILE_VERIFICATION_CODES = Arrays.<String>asList("234561", "234562", "234563");
    private static List<String> TEMP_PASSWORDS = Arrays.<String>asList("t1", "t2", "t3", "temp1", "temp2", "temp3");

    /**
     * Private constructor to prevents the class from being instantiated.
     */
    private StubDataProvider() {
    }

    /**
     * Returns a singleton StubDataProvider instance.
     *
     * @return a singleton StubDataProvider instance.
     */
    public static StubDataProvider getInstance() {
        return INSTANCE;
    }

    private static Set<String> generateEmailAddressSet(String[] userIds) {
        Set<String> set = new HashSet<String>();
        if (userIds != null) {
            for (String userId : userIds) {
                List<String> emailAddresses = getEmailAddresses(userId);
                for (String emailAddress : emailAddresses) {
                    set.add(emailAddress);
                }
            }
        }
        return set;
    }

    private static Map<String, String> generatePasswordMap(String[] userIds) {
        Map<String, String> map = new HashMap<String, String>();
        if (userIds != null) {
            for (String userId : userIds) {
                List<String> getEmailAddresses = getEmailAddresses(userId);
                for (String emailAddress : getEmailAddresses) {
                    String password = userId;
                    map.put(emailAddress, password);
                }
            }
        }
        return map;
    }

    private static List<String> getEmailAddresses(String userId) {
        List<String> emailAddresses = new ArrayList<String>();
        for (String domain : VALID_DOMAINS) {
            emailAddresses.add(userId + EMAIL_SEPARATOR + domain);
        }
        return emailAddresses;
    }

    private static boolean isValidPassword(String userId, String password) {
        String actualPassword = VALID_USER_IDS.get(userId);
        return ((userId != null) && (password != null) &&
                ((password.indexOf(actualPassword) >= 0) || (actualPassword.indexOf(password)) >= 0) || isTempPassword(password));
    }

    public static boolean isTempPassword(String password) {
        return ((password != null) && TEMP_PASSWORDS.contains(password));
    }

    public Response verifyLogin(String userId, String password) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_EMAIL_ADDRESS);
        }
        if (!VALID_USER_IDS.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_PASSWORD);
        }
        if (!isValidPassword(userId, password)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_PASSWORD);
        }
        if (!VERIFIED_EMAIL_ADDRESSES.contains(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_VERIFIED);
        }
        if (!VERIFIED_MOBILE_NUMBERS.contains(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__MOBILE_NUMBER_NOT_VERIFIED);
        }
        return Response.createSuccessResponse();
    }

    public Response verifyRegistration(String userId, String password, String name, String mobileCountryCode, String mobileNumber) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__INVALID_EMAIL_ADDRESS);
        }
        if (VALID_USER_IDS.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMAIL_ADDRESS_ALREADY_REGISTERED);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(name)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_NAME);
        }
        if (StringUtils.isNullOrEmpty(mobileCountryCode)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_MOBILE_COUNTRY_CODE);
        }
        if (StringUtils.isNullOrEmpty(mobileNumber)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_MOBILE_NUMBER);
        }
        return Response.createSuccessResponse();
    }

    public Response resetPassword(String userId) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_RESET_PASSWORD__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_RESET_PASSWORD__INVALID_EMAIL_ADDRESS);
        }
        if (!VALID_USER_IDS.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_RESET_PASSWORD__EMAIL_ADDRESS_NOT_REGISTERED);
        }
        return Response.createSuccessResponse();
    }

    public Response verifyEmailAddress(String userId, String userIdVerificationCode) {
        // TODO: More validations
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS);
        }
        if (!VALID_USER_IDS.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__EMAIL_ADDRESS_NOT_REGISTERED);
        }
        if (StringUtils.isNullOrEmpty(userIdVerificationCode)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE);
        }
        if (!EMAIL_VERIFICATION_CODES.contains(userIdVerificationCode)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE);
        }
        return Response.createSuccessResponse();
    }

    public Response verifyMobileNumber(String mobileNumber, String mobileNumberVerificationCode) {
        // TODO: More validations
        if (StringUtils.isNullOrEmpty(mobileNumber)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_MOBILE_NUMBER);
        }
        if (!MOBILE_NUMBERS.contains(mobileNumber)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__MOBILE_NUMBER_NOT_REGISTERED);
        }
        if (StringUtils.isNullOrEmpty(mobileNumberVerificationCode)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_MOBILE_NUMBER_VERIFICATION_CODE);
        }
        if (!MOBILE_VERIFICATION_CODES.contains(mobileNumberVerificationCode)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_MOBILE_NUMBER_VERIFICATION_CODE);
        }
        return Response.createSuccessResponse();
    }

    public Response changePassword(String userId, String oldPassword, String newPassword) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__INVALID_EMAIL_ADDRESS);
        }
        if (!VALID_USER_IDS.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__EMAIL_ADDRESS_NOT_REGISTERED);
        }
        if (StringUtils.isNullOrEmpty(oldPassword)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__EMPTY_OLD_PASSWORD);
        }
        if (isValidPassword(userId, oldPassword)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__INVALID_OLD_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(newPassword)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__EMPTY_NEW_PASSWORD);
        }
        if (!VERIFIED_EMAIL_ADDRESSES.contains(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_CHG_PSWD__EMAIL_ADDRESS_NOT_VERIFIED);
        }
        return Response.createSuccessResponse();
    }
}