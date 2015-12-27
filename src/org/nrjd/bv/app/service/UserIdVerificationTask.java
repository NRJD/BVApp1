/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * User id verification task.
 */
public class UserIdVerificationTask extends BaseTask {
    private String userId = null;
    private String userIdVerificationCode = null;

    public UserIdVerificationTask(String userId, String userIdVerificationCode, TaskCallback taskCallback) {
        super(taskCallback);
        this.userId = userId;
        this.userIdVerificationCode = userIdVerificationCode;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        Response response = stubDataProvider.verifyEmailAddress(this.userId, this.userIdVerificationCode);
        ResponseDataUtils.setIsUserIdVerificationTask(response, true);
        return response;
    }
}