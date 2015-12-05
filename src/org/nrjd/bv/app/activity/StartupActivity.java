/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.util.AppConstants;
import org.nrjd.bv.app.util.CommonUtils;


public class StartupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        (new StartupTask()).execute();
    }

    /**
     * The startup activity (or splash screen activity) should not be shown to the user when the user presses the back button,
     * so destroy the startup activity (or splash screen activity) when the user moves out of the startup activity (or splash screen activity)
     * to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    /**
     * Startup task to do the application startup initialization while splash screen is being shown to the user.
     */
    private class StartupTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... params) {
            // TODO: Implement the startup initialization here and sleep only for the remaining time available from SPLASH_DISPLAY_TIME.
            CommonUtils.sleep(AppConstants.SPLASH_DISPLAY_TIME);
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // TODO: Check if not already registered then, go to registration activity, otherwise to login activity.
            ActivityUtils.startLoginActivity(StartupActivity.this);
        }
    }
}