/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * Mobile number registration task.
 */
public class MobileNumberVerificationTask extends BaseTask {
    private String mobileNumber = null;
    private String mobileNumberVerificationCode = null;

    public MobileNumberVerificationTask(String mobileNumber, String mobileNumberVerificationCode, TaskCallback taskCallback) {
        super(taskCallback);
        this.mobileNumber = mobileNumber;
        this.mobileNumberVerificationCode = mobileNumberVerificationCode;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        Response response = stubDataProvider.verifyMobileNumber(this.mobileNumber, this.mobileNumberVerificationCode);
        ResponseDataUtils.setIsMobileNumberVerificationTask(response, true);
        return response;
    }
}