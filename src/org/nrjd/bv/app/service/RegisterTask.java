/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * User registration task.
 */
public class RegisterTask extends BaseTask {
    private String userId = null;
    private String password = null;
    private String name = null;
    private String mobileCountryCode = null;
    private String mobileNumber = null;

    public RegisterTask(String userId, String password, String name, String mobileCountryCode, String mobileNumber, TaskCallback taskCallback) {
        super(taskCallback);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public Response doInBackground(Void... params) {
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.verifyRegistration(this.userId, this.password, this.name, this.mobileCountryCode, this.mobileNumber);
    }
}