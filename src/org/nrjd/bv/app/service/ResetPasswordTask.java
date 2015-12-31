/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


/**
 * Reset password task.
 */
public class ResetPasswordTask extends BaseTask {
    private String userId = null;

    public ResetPasswordTask(String userId, TaskCallback taskCallback) {
        super(taskCallback);
        this.userId = userId;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.resetPassword(this.userId);
    }
}