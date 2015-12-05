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
import org.nrjd.bv.app.service.MobileNumberVerificationTask;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.ResponseDataUtils;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.service.UserIdVerificationTask;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;


public class VerifyAccountActivity extends BaseActivity implements TaskCallback {

    // Intent data parameters.
    static String USER_ID_PARAM = "UserId";
    static String MOBILE_NUMBER_PARAM = "MobileNumber";

    private AutoCompleteTextView userIdTextView = null;
    private EditText userIdVerfCodeTextView = null;
    private TextView userIdVerfStatusTextView = null;
    private Button userIdVerfButton = null;
    private AutoCompleteTextView mobileNumberTextView = null;
    private EditText mobileNumberVerfCodeTextView = null;
    private TextView mobileNumberVerfStatusTextView = null;
    private Button mobileNumberVerfButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
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
        this.progressTrackerDialog = new ProgressTrackerDialog(this, R.string.progress_title_please_wait, R.string.progress_message_email_address);
        // Initialize view elements.
        this.userIdTextView = (AutoCompleteTextView) findViewById(R.id.userId);
        this.userIdVerfCodeTextView = (EditText) findViewById(R.id.userIdVerificationCode);
        this.userIdVerfStatusTextView = (TextView) findViewById(R.id.userIdVerificationStatus);
        this.userIdVerfButton = (Button) findViewById(R.id.userIdVerificationButton);
        this.mobileNumberTextView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);
        this.mobileNumberVerfCodeTextView = (EditText) findViewById(R.id.mobileNumberVerificationCode);
        this.mobileNumberVerfStatusTextView = (TextView) findViewById(R.id.mobileNumberVerificationStatus);
        this.mobileNumberVerfButton = (Button) findViewById(R.id.mobileNumberVerificationButton);
        // Initialize fields data with the passed in intent data.
        initializeFieldData();
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize user id verification code enter event.
        this.userIdVerfCodeTextView.setOnEditorActionListener((view, actionId, event) -> {
            return handleEnterKeyEvent(view, actionId, event);
        });
        // Initialize mobile number verification code enter event.
        this.mobileNumberVerfCodeTextView.setOnEditorActionListener((view, actionId, event) -> {
            return handleEnterKeyEvent(view, actionId, event);
        });
        // Initialize verification handlers.
        this.userIdVerfButton.setOnClickListener(v -> handleUserIdVerificationCode());
        this.mobileNumberVerfButton.setOnClickListener(v -> handleMobileNumberVerificationCode());
    }

    private void initializeFieldData() {
        // Reset email address and mobile number fields
        this.userIdTextView.setText("");
        this.mobileNumberTextView.setText("");
        // Set the email address and mobile number fields data from intent.
        Bundle registrationDataParameters = getIntent().getExtras();
        if(registrationDataParameters != null) {
            String userId = registrationDataParameters.getString(VerifyAccountActivity.USER_ID_PARAM);
            String mobileNumber = registrationDataParameters.getString(VerifyAccountActivity.MOBILE_NUMBER_PARAM);
            if(StringUtils.isNotNullOrEmpty(userId)) {
                this.userIdTextView.setText(userId);
            }
            if(StringUtils.isNotNullOrEmpty(mobileNumber)) {
                this.mobileNumberTextView.setText(mobileNumber);
            }
        }
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private boolean handleEnterKeyEvent(TextView view, int actionId, KeyEvent event) {
        boolean isEnterKey = ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
        boolean isValidAction = (actionId == EditorInfo.IME_ACTION_DONE);
        if (isEnterKey || isValidAction) {
            if (view == this.userIdVerfCodeTextView) {
                handleUserIdVerificationCode();
            } else if (view == this.mobileNumberVerfCodeTextView) {
                handleMobileNumberVerificationCode();
            }
        }
        return false;
    }

    private void handleUserIdVerificationCode() {
        // Get inputs.
        String userId = this.userIdTextView.getText().toString().trim();
        String userIdVerificationCode = this.userIdVerfCodeTextView.getText().toString(); // Don't trim the verification code.
        // Validate inputs.
        if (StringUtils.isNullOrEmpty(userId)) {
            showToastMessage(getString(R.string.input_error_empty_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            showToastMessage(getString(R.string.input_error_invalid_email_address), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(userIdVerificationCode)) {
            showToastMessage(getString(R.string.input_error_empty_email_verification_code), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Validate services.
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            showToastMessage(getString(R.string.service_error_no_network_connection), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Perform login
        this.progressTrackerDialog.showProgressDialog(R.string.progress_title_please_wait, R.string.progress_message_email_address);
        (new UserIdVerificationTask(userId, userIdVerificationCode, this)).execute();
    }

    private void handleMobileNumberVerificationCode() {
        // Get inputs.
        String mobileNumber = this.mobileNumberTextView.getText().toString().trim();
        String mobileNumberVerificationCode = this.mobileNumberVerfCodeTextView.getText().toString(); // Don't trim the verification code.
        // Validate inputs.
        if (StringUtils.isNullOrEmpty(mobileNumber)) {
            showToastMessage(getString(R.string.input_error_empty_mobile_number), Toast.LENGTH_LONG);
            return; // Return from here
        }
        if (StringUtils.isNullOrEmpty(mobileNumberVerificationCode)) {
            showToastMessage(getString(R.string.input_error_empty_mobile_number_verification_code), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Validate services.
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            showToastMessage(getString(R.string.service_error_no_network_connection), Toast.LENGTH_LONG);
            return; // Return from here
        }
        // Perform login
        this.progressTrackerDialog.showProgressDialog(R.string.progress_title_please_wait, R.string.progress_message_mobile_number);
        (new MobileNumberVerificationTask(mobileNumber, mobileNumberVerificationCode, this)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            if (ResponseDataUtils.isUserIdVerificationTask(response)) {
                showToastMessage(getString(R.string.info_email_address_verification_successful), Toast.LENGTH_SHORT);
                this.userIdVerfStatusTextView.setText(getString(R.string.status_email_address_verified));
            } else if (ResponseDataUtils.isMobileNumberVerificationTask(response)) {
                showToastMessage(getString(R.string.info_mobile_number_verification_successful), Toast.LENGTH_SHORT);
                this.mobileNumberVerfStatusTextView.setText(getString(R.string.status_mobile_number_verified));
            }
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