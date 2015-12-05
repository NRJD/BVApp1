/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;


public class ProgressTrackerDialog {

    private ProgressDialog progressDialog = null;
    private Activity ownerActivity = null;
    private int initialTitleId;
    private int initialMessageId;
    private boolean isTaskInProgress = false;

    public ProgressTrackerDialog(Activity ownerActivity, int initialTitleId, int initialMessageId) {
        this.ownerActivity = ownerActivity;
        this.initialTitleId = initialTitleId;
        this.initialMessageId = initialMessageId;
    }

    private ProgressDialog createProgressDialog(int titleId, int messageId) {
        ProgressDialog dialog = new ProgressDialog(this.ownerActivity);
        dialog.setOwnerActivity(this.ownerActivity);
        dialog.setOnDismissListener(v -> {
            this.isTaskInProgress = false;
        });
        dialog.setTitle(titleId);
        dialog.setMessage(this.ownerActivity.getString(messageId));
        dialog.setIndeterminate(true);
        return dialog;
    }

    public void showProgressDialog() {
        showProgressDialog(this.initialTitleId, this.initialMessageId);
    }

    public void showProgressDialog(int titleId, int messageId) {
        if (this.progressDialog == null) {
            this.progressDialog = createProgressDialog(titleId, titleId);
        } else {
            this.progressDialog.setTitle(titleId);
            this.progressDialog.setMessage(this.ownerActivity.getString(messageId));
        }
        this.progressDialog.show();
        this.isTaskInProgress = true;
    }

    public void hideProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.hide();
        }
        this.isTaskInProgress = false;
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        this.progressDialog = null;
        this.isTaskInProgress = false;
    }

    public void showProgressDialogIfTaskInProgress() {
        if (this.isTaskInProgress && (this.progressDialog != null)) {
            this.progressDialog.show();
        }
    }

    public void hideProgressDialogIfTaskInProgress() {
        if (this.isTaskInProgress && (this.progressDialog != null)) {
            this.progressDialog.hide();
        }
    }
}