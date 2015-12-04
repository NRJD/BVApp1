/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

/**
 * Async Task: can be used to load DB, images during which the splash screen
 * is shown to user
 */
public interface TaskCallback {
    public void onTaskComplete(Response response);
    public void onTaskCancelled();
}