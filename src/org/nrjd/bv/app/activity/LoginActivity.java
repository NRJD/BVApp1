/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.net.NetworkServiceUtils;
import org.nrjd.bv.app.service.ErrorCode;
import org.nrjd.bv.app.service.LoginTask;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.StubDataTracker;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class LoginActivity extends BaseActivity implements TaskCallback {

    private AutoCompleteTextView userIdTextView = null;
    private EditText passwordTextView = null;
    private Button loginButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
     * The login activity should not be shown to the user when the user presses the back button,
     * so destroy the login activity when the user moves out of the login activity to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    private void initializeActivity() {
        // Initialize progress tracker dialog.
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_logging_in);
        // Initialize view elements.
        this.userIdTextView = (AutoCompleteTextView) findViewById(R.id.userId);
        this.passwordTextView = (EditText) findViewById(R.id.password);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        // Initialize password enter event.
        this.passwordTextView.setOnEditorActionListener((view, actionId, event) -> {
            return handlePasswordEnterEvent(view, actionId, event);
        });
        // Initialize login handler.
        this.loginButton.setOnClickListener(l -> handleLogin());
        // Initialize registration handler.
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(l -> handleRegistration());
        // Initialize forgot password handler.
        Button forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(l -> handleResetPassword());
        // Initialize verify account details handler.
        Button verifyAccountDetailsButton = (Button) findViewById(R.id.verifyAccountDetailsButton);
        verifyAccountDetailsButton.setOnClickListener(l -> handleVerifyAccountDetails());
        if(!StubDataTracker.getInstance().isUserRegistered()) {
            verifyAccountDetailsButton.setEnabled(false);
        }
    }

    private boolean handlePasswordEnterEvent(TextView view, int actionId, KeyEvent event) {
        boolean isEnterKey = ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
        boolean isValidAction = (actionId == EditorInfo.IME_ACTION_DONE);
        if (isEnterKey || isValidAction) {
            handleLogin();
        }
        return false;
    }

    private void handleLogin() {
        // Get inputs.
        String userId = this.userIdTextView.getText().toString().trim();
        String password = this.passwordTextView.getText().toString(); // Don't trim the password value.
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
        // Validate services.
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            showToastMessage(getString(R.string.service_error_no_network_connection), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Perform login.
        this.progressTrackerDialog.showProgressDialog();
        (new LoginTask(userId, password, this)).execute();
    }

    private void handleRegistration() {
        ActivityUtils.startRegisterActivity(this);
    }

    private void handleResetPassword() {
        ActivityUtils.startResetPasswordActivity(this);
    }

    private void handleVerifyAccountDetails() {
        ActivityUtils.startVerifyAccountActivity(this, null);
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastMessage(getString(R.string.info_login_successful), Toast.LENGTH_SHORT);
            ActivityUtils.startReadingActivity(this);
        } else {
            ErrorCode errorCode = Response.getErrorCodeOrGenericError(response);
            showToastMessage(getString(errorCode.getMessageId()), Toast.LENGTH_LONG);
        }
        this.progressTrackerDialog.hideProgressDialog();
    }

    @Override
    public void onTaskCancelled() {
        this.progressTrackerDialog.dismissProgressDialog();
    }
}