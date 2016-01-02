/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import android.os.AsyncTask;

import org.nrjd.bv.app.ctx.AppContext;
import org.nrjd.bv.app.service.DataServiceException;
import org.nrjd.bv.app.service.DataServiceUtils;
import org.nrjd.bv.app.service.Response;


/**
 * Base task.
 */
public abstract class BaseTask extends AsyncTask<Void, Void, Response> {
    private AppContext appContext = null;
    private TaskCallback taskCallback = null;

    public BaseTask(TaskContext taskContext) {
        if (taskContext != null) {
            this.appContext = taskContext.getAppContext();
            this.taskCallback = taskContext.getTaskCallback();
        }
    }

    protected AppContext getAppContext() {
        return this.appContext;
    }

    /**
     * Common background work for all tasks.
     */
    protected void doBackgroundWork() {
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

    protected Response constructErrorResponse(Exception e) {
        Response response = null;
        if ((e != null) & (e instanceof DataServiceException)) {
            response = DataServiceUtils.getDataServiceErrorResponse((DataServiceException) e);
        }
        return ((response != null) ? response : Response.getServiceErrorResponse());
    }
}