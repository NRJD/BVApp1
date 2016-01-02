/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.ctx.AppContext;


/**
 * Application context.
 */
public class TaskContext {
    private AppContext appContext = null;
    private TaskCallback taskCallback = null;

    public TaskContext(AppContext appContext, TaskCallback taskCallback) {
        this.appContext = appContext;
        this.taskCallback = taskCallback;
    }

    public AppContext getAppContext() {
        return this.appContext;
    }

    public TaskCallback getTaskCallback() {
        return this.taskCallback;
    }
}