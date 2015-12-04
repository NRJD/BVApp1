/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.net.NetworkServiceUtils;

public class ResetPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initializeActivity();
    }

    private void initializeActivity() {
        // Initialize login handler.
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());
        // Initialize login handler.
        Button resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void handleLogin() {
        ActivityUtils.startLoginActivity(this);
    }

    private void resetPassword() {
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            Toast.makeText(getBaseContext(), getString(R.string.error_no_network_connection), Toast.LENGTH_SHORT).show();
        } else {
            // Perform login.
            Toast.makeText(getBaseContext(), getString(R.string.info_reset_password_successful), Toast.LENGTH_SHORT).show();
            ActivityUtils.startLoginActivity(this);
            // The reset password activity should not be shown to the user when the user presses the back button,
            // so destroying the reset password activity as the user is leaving this activity at this point.
            finishActivity();
        }
    }
}