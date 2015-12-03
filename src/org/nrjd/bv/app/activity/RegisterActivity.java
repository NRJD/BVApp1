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

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The startup activity (or splash screen activity) should not be shown to the user when
        // the user presses the back button. For this, we should destroy the startup activity after
        // it is shown for few seconds. Because the onPause() method of Activity class will be called
        // when the user leaves the activity, doing this in the onPause() method by calling the finish() method.
        finish();
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
        if (!NetworkServiceUtils.isNetworkOn(getBaseContext())) {
            Toast.makeText(getBaseContext(), getString(R.string.error_no_network_connection), Toast.LENGTH_SHORT).show();
        } else {
            // Perform registration.
            Toast.makeText(getBaseContext(), getString(R.string.info_registration_successful), Toast.LENGTH_SHORT).show();
            ActivityUtils.startLoginActivity(this);
        }
    }
}