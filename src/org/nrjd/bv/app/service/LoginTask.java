/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

import android.os.AsyncTask;

import org.nrjd.bv.app.util.CommonUtils;

/**
 * Async Task: can be used to load DB, images during which the splash screen
 * is shown to user
 */
public class LoginTask extends AsyncTask<Void, Void, Response> {
    private String userId = null;
    private String password = null;
    private TaskCallback taskCallback = null;

    public LoginTask(String userId, String password, TaskCallback taskCallback) {
        this.userId = userId;
        this.password = password;
        this.taskCallback = taskCallback;
    }

    @Override
    public Response doInBackground(Void... params) {
        // TODO: Remove the sleep call.
        CommonUtils.sleep(2000);
        StubDataProvider stubDataProvider = StubDataProvider.getInstance();
        return stubDataProvider.verifyLogin(this.userId, this.password);
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (this.taskCallback != null) {
            this.taskCallback.onTaskComplete(response);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (this.taskCallback != null) {
            this.taskCallback.onTaskCancelled();
        }
    }
}