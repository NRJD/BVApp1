/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.nrjd.bv.app.R;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.task.ResendAccountActivationTask;
import org.nrjd.bv.app.util.ErrorCode;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class ResendAccountActivationActivity extends BaseTaskActivity {

    private AutoCompleteTextView userIdTextView = null;
    private Button resendAccountActivationButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_account_activation);
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
     * The resend account activation details activity should not be shown to the user when the user presses the back button,
     * so destroy the resend account activation details activity when the user moves out of the resend account activation details
     * to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    private void initializeActivity() {
        // Initialize progress tracker dialog.
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_resend_account_activation_details);
        this.resendAccountActivationButton = (Button) findViewById(R.id.resendAccountActivationButton);
        // Initialize view elements.
        this.userIdTextView = (AutoCompleteTextView) findViewById(R.id.userId);
        // Initialize fields data with the passed in intent data.
        initializeFieldData();
        // Initialize activate account handler.
        Button activateAccountButton = (Button) findViewById(R.id.activateAccountButton);
        activateAccountButton.setOnClickListener(v -> handleActivateAccount());
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize resend account activation details handler.
        this.resendAccountActivationButton.setOnClickListener(v -> handleResendAccountActivationDetails());
    }

    private void initializeFieldData() {
        // Reset email address and mobile number fields
        this.userIdTextView.setText("");
        // Set the email address data from intent.
        Bundle accountActivationDataParameters = getIntent().getExtras();
        if (accountActivationDataParameters != null) {
            String userId = accountActivationDataParameters.getString(ActivityParameters.USER_ID_PARAM);
            if (StringUtils.isNotNullOrEmpty(userId)) {
                this.userIdTextView.setText(userId);
            }
        }
    }

    private void handleActivateAccount() {
        Bundle accountActivationDataParameters = getAccountActivationDataParameters();
        ActivityUtils.startVerifyAccountActivity(this, accountActivationDataParameters);
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void handleResendAccountActivationDetails() {
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
        (new ResendAccountActivationTask(getTaskContext(), userId)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastAlertInfoMessage(getString(R.string.info_resend_account_activation_details_successful));
            handleActivateAccount();
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

    private Bundle getAccountActivationDataParameters() {
        String userId = this.userIdTextView.getText().toString().trim();
        Bundle accountActivationDataParameters = new Bundle();
        accountActivationDataParameters.putString(ActivityParameters.USER_ID_PARAM, userId);
        return accountActivationDataParameters;
    }
}