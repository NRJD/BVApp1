/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

import android.os.AsyncTask;

import org.nrjd.bv.app.util.AppConstants;
import org.nrjd.bv.app.util.CommonUtils;


/**
 * User registration task.
 */
public abstract class BaseTask extends AsyncTask<Void, Void, Response> {
    private TaskCallback taskCallback = null;

    public BaseTask(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
    }

    @Override
    protected void onPreExecute() {
        // TODO: Remove the sleep call.
        CommonUtils.sleep(AppConstants.STUB_DATA_SERVICE_VALIDATION_TIME);
    }

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