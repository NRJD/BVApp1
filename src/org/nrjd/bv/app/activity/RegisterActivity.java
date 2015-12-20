/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.metadata.CountryCallingCode;
import org.nrjd.bv.app.metadata.CountryCallingCodeUtils;
import org.nrjd.bv.app.service.ErrorCode;
import org.nrjd.bv.app.service.RegisterTask;
import org.nrjd.bv.app.service.Response;
import org.nrjd.bv.app.service.TaskCallback;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;
import org.nrjd.bv.app.util.SystemUtils;
import org.nrjd.bv.app.util.TelephonyUtils;

import java.util.List;


public class RegisterActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemClickListener {

    private AutoCompleteTextView userIdTextView = null;
    private EditText passwordTextView = null;
    private EditText confirmPasswordTextView = null;
    private AutoCompleteTextView userNameTextView = null;
    private View mobileCountryCodeLayoutView = null;
    private TextView mobileCountryCodeTextView = null;
    private AutoCompleteTextView mobileNumberTextView = null;
    private Button signUpButton = null;
    private ProgressTrackerDialog progressTrackerDialog = null;
    private ArrayAdapter<CountryCallingCode> countryCallingCodeListAdapter = null;
    private ListPopupWindow countryCallingCodeListPopupWindow = null;
    private CountryCallingCode selectedCountryCallingCode = null;

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
        this.mobileCountryCodeLayoutView = findViewById(R.id.mobileCountryCodeLayout);
        this.mobileCountryCodeTextView = (TextView) findViewById(R.id.mobileCountryCodeText);
        this.mobileNumberTextView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);
        this.signUpButton = (Button) findViewById(R.id.signUpButton);
        // Initialize initialize mobile country calling code handler.
        initializeCountryCallingCodeHandler();
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize registration handler.
        this.signUpButton.setOnClickListener(v -> handleRegistration());
    }

    private void initializeCountryCallingCodeHandler() {
        // Initialize country calling code list adapter.
        this.countryCallingCodeListAdapter = new ArrayAdapter<CountryCallingCode>(this, android.R.layout.simple_list_item_1);
        List<CountryCallingCode> countryCallingCodes = CountryCallingCodeUtils.getCountryCallingCodes();
        if (countryCallingCodes != null) {
            for (CountryCallingCode countryCallingCode : countryCallingCodes) {
                this.countryCallingCodeListAdapter.add(countryCallingCode);
            }
        }
        // Initialize country calling code list popup window.
        this.countryCallingCodeListPopupWindow = new ListPopupWindow(this);
        this.countryCallingCodeListPopupWindow.setAdapter(this.countryCallingCodeListAdapter);
        // Set the selection.
        String isoCountryCode = TelephonyUtils.getSimCountryIso(this);
        int selectedIndex = CountryCallingCodeUtils.getSelectedIndex(isoCountryCode);
        if (selectedIndex >= 0) {
            this.countryCallingCodeListPopupWindow.setSelection(selectedIndex);
            updateMobileCountryCodeText(selectedIndex);
        }
        // Set the screen positioning.
        this.countryCallingCodeListPopupWindow.setAnchorView(this.userIdTextView);
        this.countryCallingCodeListPopupWindow.setHorizontalOffset(75);
        this.countryCallingCodeListPopupWindow.setVerticalOffset(5);
        // Set width and height for the popup as relative percentage of the screen dimensions
        // so that user can press outside of the list popup window to dismiss it.
        int screenWidthPixels = SystemUtils.getScreenWidthPixels(this);
        int screenHeightPixels = SystemUtils.getScreenHeightPixels(this);
        int popupWindowWidth = ((screenWidthPixels > 0) ? (screenWidthPixels * 75 / 100) : 400);
        int popupWindowHeight = ((screenHeightPixels > 0) ? (screenHeightPixels * 60 / 100) : 400);
        this.countryCallingCodeListPopupWindow.setWidth(popupWindowWidth);
        this.countryCallingCodeListPopupWindow.setHeight(popupWindowHeight);
        // Set other properties.
        this.countryCallingCodeListPopupWindow.setModal(true);
        this.countryCallingCodeListPopupWindow.setOnItemClickListener(this);
        // Attach onclick event to the mobile country calling code text view and layout.
        this.mobileCountryCodeTextView.setOnClickListener(v -> showCountryCallingCodeListPopup());
        mobileCountryCodeLayoutView.setOnClickListener(v -> showCountryCallingCodeListPopup());
    }

    private void showCountryCallingCodeListPopup() {
        this.countryCallingCodeListPopupWindow.show();
        // If the soft keyboard is open, then dismiss it before opening the list popup window.
        SystemUtils.dismissKeyboard(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        updateMobileCountryCodeText(position);
        this.countryCallingCodeListPopupWindow.dismiss();
    }

    public void updateMobileCountryCodeText(int selectedIndex) {
        this.selectedCountryCallingCode = this.countryCallingCodeListAdapter.getItem(selectedIndex);
        String isoCodeDisplayString = ((this.selectedCountryCallingCode != null) ? this.selectedCountryCallingCode.getISOCodeDisplayString() : getString(R.string.select));
        this.mobileCountryCodeTextView.setText(isoCodeDisplayString);
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
        String mobileCountryCode = ((this.selectedCountryCallingCode != null) ? this.selectedCountryCallingCode.getCallingCode() : null);
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
        if (userName.length() < 3) {
            showToastMessage(getString(R.string.input_error_insufficient_name_length), Toast.LENGTH_LONG);
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
        // Perform login
        this.progressTrackerDialog.showProgressDialog();
        (new RegisterTask(userId, password, userName, mobileCountryCode, mobileNumber, this)).execute();
    }

    @Override
    public void onTaskComplete(Response response) {
        if ((response != null) && response.isSuccess()) {
            showToastMessage(getString(R.string.info_registration_successful), Toast.LENGTH_SHORT);
            Bundle registrationDataParameters = getRegistrationDataParameters();
            ActivityUtils.startVerifyAccountActivity(this, registrationDataParameters);
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

    private Bundle getRegistrationDataParameters() {
        String userId = this.userIdTextView.getText().toString().trim();
        String mobileNumber = this.mobileNumberTextView.getText().toString().trim();
        Bundle registrationDataParameters = new Bundle();
        registrationDataParameters.putString(VerifyAccountActivity.USER_ID_PARAM, userId);
        registrationDataParameters.putString(VerifyAccountActivity.MOBILE_NUMBER_PARAM, mobileNumber);
        return registrationDataParameters;
    }
}