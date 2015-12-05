/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.net.NetworkServiceUtils;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeActivity();
    }

    private void initializeActivity() {
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize registration handler.
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> handleRegistration());
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void handleRegistration() {
        // TODO: Add utility to get it
//        String userId = this.userIdTextView.getText().toString().trim();
//        String password = this.passwordTextView.getText().toString(); // Don't trim the password value.
//        if (StringUtils.isNullOrEmpty(userId)) {
//            showToastMessage(getString(R.string.error_empty_email_address), Toast.LENGTH_LONG);
//            return; // Return from here
//        }
//        if (!PatternUtils.isValidEmailAddress(userId)) {
//            showToastMessage(getString(R.string.error_invalid_email_address), Toast.LENGTH_LONG);
//            return; // Return from here
//        }
//        if (StringUtils.isNullOrEmpty(password)) {
//            showToastMessage(getString(R.string.error_empty_password), Toast.LENGTH_LONG);
//            return; // Return from here
//        }
//        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
//            showToastMessage(getString(R.string.error_no_network_connection), Toast.LENGTH_LONG);
//            return; // Return from here
//        }
//        // Perform login
//        showProgressDialog();
//        (new LoginTask(userId, password, this)).execute();
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            Toast.makeText(getBaseContext(), getString(R.string.error_no_network_connection), Toast.LENGTH_SHORT).show();
        } else {
            EditText passwordTextView = (EditText) findViewById(R.id.password);
            String password = passwordTextView.getText().toString(); // Don't trim the password value.
            if ((password.length() < 6) || (password.length() > 10)) {
                showToastMessage(getString(R.string.error_password_length_mismatch), Toast.LENGTH_LONG);
                return; // Return from here
            }
            // Perform registration.
            Toast.makeText(getBaseContext(), getString(R.string.info_registration_successful), Toast.LENGTH_SHORT).show();
            ActivityUtils.startLoginActivity(this);
            // The register activity should not be shown to the user when the user presses the back button,
            // so destroying the register activity as the user is leaving this activity at this point.
            finishActivity();
        }
    }
}