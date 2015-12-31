/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.nrjd.bv.app.R;

import org.nrjd.bv.app.service.ErrorCode;
import org.nrjd.bv.app.service.ResetPasswordTask;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class ResetPasswordActivity extends BaseActivity implements TaskCallback {

    private AutoCompleteTextView userIdTextView = null;
    private Button resetPasswordButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initializeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.progressTrackerDialog.showProgressDialogIfTaskInProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.progressTrackerDialog.hideProgressDialogIfTaskInProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.progressTrackerDialog.dismissProgressDialog();
    }

    /**
     * The reset password activity should not be shown to the user when the user presses the back button,
     * so destroy the reset password activity when the user moves out of the reset password to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    private void initializeActivity() {
        // Initialize progress tracker dialog.
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_reset_password);
        this.resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        // Initialize view elements.
        this.userIdTextView = (AutoCompleteTextView) findViewById(R.id.userId);
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize reset password handler.
        this.resetPasswordButton.setOnClickListener(v -> handleResetPassword());
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void handleResetPassword() {
        // Get inputs.
        String userId = this.userIdTextView.getText().toString().trim();
        // Validate inputs.
        if (StringUtils.isNullOrEmpty(userId)) {
            showToastErrorMessage(getString(R.string.input_error_empty_email_address));
            return; // Return from here
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            showToastErrorMessage(getString(R.string.input_error_invalid_email_address));
            return; // Return from here
        }
        // Perform login
        this.progressTrackerDialog.showProgressDialog();
        (new ResetPasswordTask(userId, this)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastAlertInfoMessage(getString(R.string.info_reset_password_successful));
            ActivityUtils.startLoginActivity(this);
        } else {
            ErrorCode errorCode = Response.getErrorCodeOrGenericError(response);
            showToastErrorMessage(getString(errorCode.getMessageId()));
        }
        this.progressTrackerDialog.hideProgressDialog();
    }

    @Override
    public void onTaskCancelled() {
        this.progressTrackerDialog.dismissProgressDialog();
    }
}