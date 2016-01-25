/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.ResponseDataUtils;
import org.nrjd.bv.app.session.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User login task.
 */
public class LoginTask extends BaseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginTask.class);
    private String userId = null;
    private String password = null;

    public LoginTask(TaskContext taskContext, String userId, String password) {
        super(taskContext);
        this.userId = userId;
        this.password = password;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        Response response = null;
        try {
            response = getDataServiceProvider().performLogin(this.userId, this.password);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while performing the user login", e);
            response = constructErrorResponse(e);
        }
        ResponseDataUtils.setUserLoginDetails(response, new UserLogin(this.userId, this.password));
        return response;
    }
}