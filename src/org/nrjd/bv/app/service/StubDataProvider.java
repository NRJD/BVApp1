/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class StubDataProvider {
    private static final StubDataProvider INSTANCE = new StubDataProvider();

    private Map<String, String> validLogins = generatePasswordMap(new String[]{"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"});
    private Set<String> verifiedEmailAddresses = generateEmailAddressSet(new String[]{"user1", "user2", "user3", "user4", "user5"});
    private Set<String> verifiedMobileNumbers = generateEmailAddressSet(new String[]{"user1", "user2", "user3"});
    private static final String EMAIL_DOMAIN = "@nrjd.com";

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

    public Response verifyLogin(String userId, String password) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_EMAIL_ADDRESS);
        }
        if (!validLogins.containsKey(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED);
        } else {
            String userPassword = validLogins.get(userId);
            if((password == null) || (userPassword == null) || (!(password.indexOf(userPassword) >= 0))) {
                return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_PASSWORD);
            }
        }
        if (!verifiedEmailAddresses.contains(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_VERIFIED);
        }
        if (!verifiedMobileNumbers.contains(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__MOBILE_NUMBER_NOT_VERIFIED);
        }
        return Response.createSuccessResponse();
    }

    private static Set<String> generateEmailAddressSet(String[] userIds) {
        Set<String> set = new HashSet<String>();
        if (userIds != null) {
            for (String userId : userIds) {
                String emailAddress = getEmailAddress(userId);
                set.add(emailAddress);
            }
        }
        return set;
    }

    private static Map<String, String> generatePasswordMap(String[] userIds) {
        Map<String, String> map = new HashMap<String, String>();
        if (userIds != null) {
            for (String userId : userIds) {
                String emailAddress = getEmailAddress(userId);
                String password = userId;
                map.put(emailAddress, password);
            }
        }
        return map;
    }

    private static String getEmailAddress(String userId) {
        return userId + EMAIL_DOMAIN;
    }
}