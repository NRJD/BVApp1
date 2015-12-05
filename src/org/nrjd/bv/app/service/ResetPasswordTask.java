/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * User registration task.
 */
public class ResetPasswordTask extends BaseTask {
    private String userId = null;

    public ResetPasswordTask(String userId, TaskCallback taskCallback) {
        super(taskCallback);
        this.userId = userId;
    }

    @Override
    public Response doInBackground(Void... params) {
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.resetPassword(this.userId);
    }
}