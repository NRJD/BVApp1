/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.session;


/**
 * User session.
 */
public class UserSession {
    private UserLogin userLogin = null;
    private boolean isLoggedIn = false;
    private String lastLogin = null;

    public UserSession() {
    }

    public UserLogin getUserLogin() {
        return this.userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserId() {
        return ((this.userLogin != null) ? this.userLogin.getUserId() : null);
    }

    public String getPassword() {
        return ((this.userLogin != null) ? this.userLogin.getPassword() : null);
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}