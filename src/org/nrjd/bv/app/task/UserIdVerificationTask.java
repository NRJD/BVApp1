/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.ResponseDataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User id verification task.
 */
public class UserIdVerificationTask extends BaseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserIdVerificationTask.class);
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
        Response response = null;
        try {
            response = getDataServiceProvider().verifyUserId(this.userId, this.userIdVerificationCode);
        } catch (Exception e) {
            LOGGER.debug("Error while verifying the user id", e);
            response = constructErrorResponse(e);
        }
        ResponseDataUtils.setIsUserIdVerificationTask(response, true);
        return response;
    }
}