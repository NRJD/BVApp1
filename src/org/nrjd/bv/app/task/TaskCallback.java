/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.service.Response;


/**
 * Task callback interface.
 */
public interface TaskCallback {
    public void onTaskComplete(Response response);

    public void onTaskCancelled();
}