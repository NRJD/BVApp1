/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base session manager.
 */
public abstract class BaseSessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSessionManager.class);

    private static final int PRIVATE_MODE = 0;

    private Context context;
    private SharedPreferences prefs;

    protected BaseSessionManager(Context context, String preferencesFileName) {
        this.context = context;
        this.prefs = context.getSharedPreferences(preferencesFileName, PRIVATE_MODE);
    }

    protected SharedPreferences getPreferences() {
        return this.prefs;
    }

    protected Editor getPreferencesEditor() {
        return this.prefs.edit();
    }

    protected void cleanSession() {
        Editor editor = this.prefs.edit();
        editor.clear();
        editor.commit();
    }
}