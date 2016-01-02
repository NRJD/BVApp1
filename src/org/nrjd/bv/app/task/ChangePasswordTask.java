/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.StubDataProvider;


/**
 * Change password task.
 */
public class ChangePasswordTask extends BaseTask {
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
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.changePassword(this.userId, this.oldPassword, this.newPassword);
    }
}