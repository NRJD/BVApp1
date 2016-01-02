/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import org.nrjd.bv.app.task.TaskCallback;
import org.nrjd.bv.app.task.TaskContext;


/**
 * Base activity class for all those activities that use background task events.
 */
public abstract class BaseTaskActivity extends BaseActivity implements TaskCallback {
    protected TaskContext getTaskContext() {
        return new TaskContext(ActivityUtils.getAppContext(this), this);
    }
}