/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.ctx;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.nrjd.bv.app.util.StringUtils;

import java.io.File;


/**
 * Application context.
 */
public class AppContext {
    private static final String DOWNLOADS_FOLDER_NAME = "Downloads";
    private static final String TEST_FOLDER_NAME = "Test";

    private Context context = null;

    public AppContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public File getDownloadsFolder() {
        return getAppDataFolder(DOWNLOADS_FOLDER_NAME);
    }

    public File getDownloadsTestFolder() {
        File downloadsFolder = getDownloadsFolder();
        return ((downloadsFolder != null) ? new File(downloadsFolder, TEST_FOLDER_NAME) : null);
    }

    private File getAppDataFolder(String subFolderName) {
        if ((this.context != null) && StringUtils.isNotNullOrEmpty(subFolderName)) {
            File[] files = ContextCompat.getExternalFilesDirs(this.context, subFolderName);
            return (((files != null) && (files.length > 0)) ? files[0] : null);
        }
        return null;
    }
}