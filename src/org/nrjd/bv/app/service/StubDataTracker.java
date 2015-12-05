/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

public class StubDataTracker {
    private static final StubDataTracker INSTANCE = new StubDataTracker();

    private boolean isUserRegistered = false;

    /**
     * Private constructor to prevents the class from being instantiated.
     */
    private StubDataTracker() {
    }

    /**
     * Returns a singleton StubDataTracker instance.
     *
     * @return a singleton StubDataTracker instance.
     */
    public static StubDataTracker getInstance() {
        return INSTANCE;
    }

    public boolean isUserRegistered() {
        return this.isUserRegistered;
    }

    public void setIsUserRegistered(boolean isUserRegistered) {
        this.isUserRegistered = isUserRegistered;
    }
}