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
 * User id verification task.
 */
public class UserIdVerificationTask extends BaseTask {
    private String userId = null;
    private String userIdVerificationCode = null;

    public UserIdVerificationTask(TaskContext taskContext, String userId, String userIdVerificationCode) {
        super(taskContext);
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