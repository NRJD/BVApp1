/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

public class UserRegUtils {
    private static final UserRegUtils INSTANCE = new UserRegUtils();

    /**
     * Private constructor to prevents the class from being instantiated.
     */
    private UserRegUtils() {
    }

    /**
     * Returns a singleton UserRegUtils instance.
     *
     * @return a singleton UserRegUtils instance.
     */
    public static UserRegUtils getInstance() {
        return INSTANCE;
    }

    public boolean isUserRegistered() {
        // TODO: For now always returning true.
        // Once we track user registration details in database, then this can return flag from database.
        return true;
    }
}