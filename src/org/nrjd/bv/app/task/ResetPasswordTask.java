/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.StubDataProvider;


/**
 * Reset password task.
 */
public class ResetPasswordTask extends BaseTask {
    private String userId = null;

    public ResetPasswordTask(TaskContext taskContext, String userId) {
        super(taskContext);
        this.userId = userId;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.resetPassword(this.userId);
    }
}