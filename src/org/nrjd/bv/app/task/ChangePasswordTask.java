/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Change password task.
 */
public class ChangePasswordTask extends BaseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTask.class);
    private String userId = null;
    private String oldPassword = null;
    private String newPassword = null;

    public ChangePasswordTask(TaskContext taskContext, String userId, String oldPassword, String newPassword) {
        super(taskContext);
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        Response response = null;
        try {
            response = getDataServiceProvider().changePassword(this.userId, this.oldPassword, this.newPassword);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while performing the user login", e);
            response = constructErrorResponse(e);
        }
        return response;
    }
}