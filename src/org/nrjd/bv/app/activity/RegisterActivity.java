/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.net.NetworkServiceUtils;
import org.nrjd.bv.app.service.ErrorCode;
import org.nrjd.bv.app.service.RegisterTask;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class RegisterActivity extends BaseActivity implements TaskCallback {

    private AutoCompleteTextView userIdTextView = null;
    private EditText passwordTextView = null;
    private EditText confirmPasswordTextView = null;
    private AutoCompleteTextView userNameTextView = null;
    private AutoCompleteTextView mobileCountryCodeTexView = null;
    private AutoCompleteTextView mobileNumberTextView = null;
    private Button signUpButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_registering);
        // Initialize view elements.
        this.userIdTextView = (AutoCompleteTextView) findViewById(R.id.userId);
        this.passwordTextView = (EditText) findViewById(R.id.password);
        this.confirmPasswordTextView = (EditText) findViewById(R.id.confirmPassword);
        this.userNameTextView = (AutoCompleteTextView) findViewById(R.id.userName);
        this.mobileCountryCodeTexView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);
        this.mobileNumberTextView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);
        this.signUpButton = (Button) findViewById(R.id.signUpButton);
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize registration handler.
        this.signUpButton.setOnClickListener(v -> handleRegistration());
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void handleRegistration() {
        // Get inputs.
        String userId = this.userIdTextView.getText().toString().trim();
        String password = this.passwordTextView.getText().toString(); // Don't trim the password value.
        String confirmPassword = this.confirmPasswordTextView.getText().toString(); // Don't trim the password value.
        String userName = this.userNameTextView.getText().toString().trim();
        String mobileCountryCode = this.mobileCountryCodeTexView.getText().toString().trim();
        String mobileNumber = this.mobileNumberTextView.getText().toString().trim();
        // Validate inputs.
        if (StringUtils.isNullOrEmpty(userId)) {
            showToastMessage(getString(R.string.input_error_empty_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            showToastMessage(getString(R.string.input_error_invalid_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(password)) {
            showToastMessage(getString(R.string.input_error_empty_password), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(confirmPassword)) {
            showToastMessage(getString(R.string.input_error_empty_confirm_password), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if ((password.length() < 6) || (password.length() > 15)) {
            showToastMessage(getString(R.string.input_error_password_length_mismatch), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (!password.equals(confirmPassword)) {
            showToastMessage(getString(R.string.input_error_passwords_mismatch), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(userName)) {
            showToastMessage(getString(R.string.input_error_empty_name), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(mobileCountryCode)) {
            showToastMessage(getString(R.string.input_error_empty_mobile_country_code), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(mobileNumber)) {
            showToastMessage(getString(R.string.input_error_empty_mobile_number), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Validate services.
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            showToastMessage(getString(R.string.service_error_no_network_connection), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Perform login
        this.progressTrackerDialog.showProgressDialog();
        (new RegisterTask(userId, password, userName, mobileCountryCode, mobileNumber, this)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastMessage(getString(R.string.info_registration_successful), Toast.LENGTH_SHORT);
            ActivityUtils.startLoginActivity(this);
        } else {
            ErrorCode errorCode = Response.getErrorCodeOrGenericError(response);
            showToastMessage(getString(errorCode.getMessageId()), Toast.LENGTH_LONG);
            this.progressTrackerDialog.hideProgressDialog();
        }
    }

    @Override
    public void onTaskCancelled() {
        this.progressTrackerDialog.dismissProgressDialog();
    }
}