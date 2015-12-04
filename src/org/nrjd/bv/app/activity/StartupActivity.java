/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import net.nightwhistler.pageturner.R;

import org.nrjd.bv.app.util.CommonUtils;

public class StartupActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        (new StartupTask()).execute();
    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class StartupTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... params) {
            // TODO: Implement the startup initialization here and sleep only for the remaining time available from SPLASH_DISPLAY_TIME.
            CommonUtils.sleep(SPLASH_DISPLAY_TIME);
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // TODO: Check if not already registered then, go to registration activity, otherwise to login activity.
            ActivityUtils.startLoginActivity(StartupActivity.this);
            // The startup activity (or splash screen activity) should not be shown to the user when the user presses the back button,
            // so destroying the startup activity (or splash screen activity) as the user is leaving this activity at this point.
            finishActivity();
            finishActivity();
        }
    }
}