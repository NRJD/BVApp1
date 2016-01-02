/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.ResponseDataUtils;
import org.nrjd.bv.app.service.StubDataProvider;


/**
 * Mobile number registration task.
 */
public class MobileNumberVerificationTask extends BaseTask {
    private String mobileNumber = null;
    private String mobileNumberVerificationCode = null;

    public MobileNumberVerificationTask(TaskContext taskContext, String mobileNumber, String mobileNumberVerificationCode) {
        super(taskContext);
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