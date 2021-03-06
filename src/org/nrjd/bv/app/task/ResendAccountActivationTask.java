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
 * Resend account activation details task.
 */
public class ResendAccountActivationTask extends BaseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResendAccountActivationTask.class);
    private String userId = null;

    public ResendAccountActivationTask(TaskContext taskContext, String userId) {
        super(taskContext);
        this.userId = userId;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        Response response = null;
        try {
            response = getDataServiceProvider().resendAccountActivationDetails(this.userId);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while resetting the user password", e);
            response = constructErrorResponse(e);
        }
        return response;
    }
}