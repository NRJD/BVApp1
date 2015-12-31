/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * User login task.
 */
public class LoginTask extends BaseTask {
    private String userId = null;
    private String password = null;

    public LoginTask(String userId, String password, TaskCallback taskCallback) {
        super(taskCallback);
        this.userId = userId;
        this.password = password;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        Response response = stubDataProvider.verifyLogin(this.userId, this.password);
        ResponseDataUtils.setIsTempPassword(response, StubDataProvider.isTempPassword(this.password));
        return response;
    }
}