/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.ProgressDialog;
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
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;

public class LoginActivity extends BaseActivity implements TaskCallback {

    private AutoCompleteTextView userIdTextView = null;
    private EditText passwordTextView = null;
    private Button loginButton = null;
    private ProgressDialog progressDialog = null;
    private boolean isTaskInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressDialogIfTaskInProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialogIfTaskInProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    private void initializeActivity() {
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
        // Initialize registration handler.
        Button forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(l -> handleForgotPassword());
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
        // TODO: Add utility to get it
        String userId = this.userIdTextView.getText().toString().trim();
        String password = this.passwordTextView.getText().toString(); // Don't trim the password value.
        if (StringUtils.isNullOrEmpty(userId)) {
            showToastMessage(getString(R.string.error_empty_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            showToastMessage(getString(R.string.error_invalid_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(password)) {
            showToastMessage(getString(R.string.error_empty_password), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            showToastMessage(getString(R.string.error_no_network_connection), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Perform login
        showProgressDialog();
        (new LoginTask(userId, password, this)).execute();
    }

    private void handleRegistration() {
        ActivityUtils.startRegisterActivity(this);
    }

    private void handleForgotPassword() {
        ActivityUtils.startResetPasswordActivity(this);
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastMessage(getString(R.string.info_login_successful), Toast.LENGTH_SHORT);
            ActivityUtils.startReadingActivity(this);
            // The login activity should not be shown to the user when the user presses the back button,
            // so destroying the login activity as the user is leaving this activity at this point.
            finishActivity();
        } else {
            ErrorCode errorCode = Response.getErrorCodeOrGenericError(response);
            showToastMessage(getString(errorCode.getMessageId()), Toast.LENGTH_LONG);
            hideProgressDialog();
        }
    }

    @Override
    public void onTaskCancelled() {
        hideProgressDialog();
    }

    private void showProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = createProgressDialog();
        }
        this.progressDialog.show();
        this.isTaskInProgress = true;
    }

    private void hideProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.hide();
        }
        this.isTaskInProgress = false;
    }

    private void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        this.progressDialog = null;
        this.isTaskInProgress = false;
    }

    private void showProgressDialogIfTaskInProgress() {
        if (this.isTaskInProgress && (this.progressDialog != null)) {
            this.progressDialog.show();
        }
    }

    private void hideProgressDialogIfTaskInProgress() {
        if (this.isTaskInProgress && (this.progressDialog != null)) {
            this.progressDialog.hide();
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setOwnerActivity(this);
        dialog.setOnDismissListener(v -> {
            this.isTaskInProgress = false;
        });
        dialog.setTitle(R.string.progress_title_please_wait);
        dialog.setMessage(getString(R.string.progress_message_logging_in));
        dialog.setIndeterminate(true);
        return dialog;
    }
}