/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.service.ChangePasswordTask;
import org.nrjd.bv.app.service.ErrorCode;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class ChangePasswordActivity extends BaseActivity implements TaskCallback {

    private TextView userIdTextView = null;
    private String userId = null;
    private EditText oldPasswordTextView = null;
    private EditText newPasswordTextView = null;
    private EditText confirmNewPasswordTextView = null;
    private Button changePasswordButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
     * The register activity should not be shown to the user when the user presses the back button,
     * so destroy the register activity when the user moves out of the register activity to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    private void initializeActivity() {
        // Initialize progress tracker dialog.
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_updating_the_new_password);
        // Initialize view elements.
        this.userIdTextView = (TextView) findViewById(R.id.userIdText);
        this.oldPasswordTextView = (EditText) findViewById(R.id.oldPassword);
        this.newPasswordTextView = (EditText) findViewById(R.id.newPassword);
        this.confirmNewPasswordTextView = (EditText) findViewById(R.id.confirmNewPassword);
        this.changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        // Initialize fields data with the passed in intent data.
        initializeFieldData();
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize registration handler.
        this.changePasswordButton.setOnClickListener(v -> handleChangePassword());
    }

    private void initializeFieldData() {
        // Reset email address field.
        this.userIdTextView.setText("");
        // Set the email address field data from intent.
        Bundle loginDataParameters = getIntent().getExtras();
        if (loginDataParameters != null) {
            this.userId = loginDataParameters.getString(ActivityParameters.USER_ID_PARAM);
            if (StringUtils.isNotNullOrEmpty(this.userId)) {
                String userIdTextLabel = String.format(getString(R.string.user_id_text_label), this.userId);
                this.userIdTextView.setText(userIdTextLabel);
            }
        }
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void handleChangePassword() {
        // Get inputs.
        String oldPassword = this.oldPasswordTextView.getText().toString(); // Don't trim the password value.
        String newPassword = this.newPasswordTextView.getText().toString(); // Don't trim the password value.
        String confirmNewPassword = this.confirmNewPasswordTextView.getText().toString(); // Don't trim the password value.
        // Validate inputs.
        if (StringUtils.isNullOrEmpty(this.userId)) {
            showToastErrorMessage(getString(R.string.input_error_empty_email_address_for_change_password));
            return; // Return from here
        }
        if (!PatternUtils.isValidEmailAddress(this.userId)) {
            showToastErrorMessage(getString(R.string.input_error_invalid_email_address_for_change_password));
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(oldPassword)) {
            showToastErrorMessage(getString(R.string.input_error_empty_old_password));
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(newPassword)) {
            showToastErrorMessage(getString(R.string.input_error_empty_new_password));
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(confirmNewPassword)) {
            showToastErrorMessage(getString(R.string.input_error_empty_confirm_new_password));
            return; // Return from here
        }
        if ((newPassword.length() < 6) || (newPassword.length() > 15)) {
            showToastErrorMessage(getString(R.string.input_error_new_password_length_mismatch));
            return; // Return from here
        }
        if (newPassword.equals(oldPassword)) {
            showToastErrorMessage(getString(R.string.input_error_new_password_same_as_old_password));
            return; // Return from here
        }
        if (!newPassword.equals(confirmNewPassword)) {
            showToastErrorMessage(getString(R.string.input_error_new_passwords_mismatch));
            return; // Return from here
        }
        // Perform login
        this.progressTrackerDialog.showProgressDialog();
        (new ChangePasswordTask(this.userId, oldPassword, newPassword, this)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastAlertInfoMessage(getString(R.string.info_change_password_successful));
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