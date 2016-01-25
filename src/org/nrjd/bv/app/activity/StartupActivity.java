/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import org.nrjd.bv.app.R;
import org.nrjd.bv.app.session.UserSessionUtils;
import org.nrjd.bv.app.util.CommonUtils;

import java.util.Date;

import static org.nrjd.bv.app.util.AppConstants.SPLASH_DISPLAY_TIME;


public class StartupActivity extends BaseActivity {
    private Date startTime = null;
    private boolean isUserLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        (new StartupTask()).execute();
        StartupActivity.this.startTime = new Date();
    }

    /**
     * The startup activity (or splash screen activity) should not be shown to the user when the user presses the back button,
     * so destroy the startup activity (or splash screen activity) when the user moves out of the startup activity (or splash screen activity)
     * to some other activity.
     */
    public boolean retainActivityInBackButtonHistory() {
        return false;
    }

    private long remainingSleepTimeInMillis() {
        if (this.startTime != null) {
            Date currentTime = new Date();
            long diff = Math.abs(currentTime.getTime() - this.startTime.getTime());
            return ((diff < SPLASH_DISPLAY_TIME) ? SPLASH_DISPLAY_TIME - diff : 0);
        } else {
            return SPLASH_DISPLAY_TIME;
        }
    }

    /**
     * Startup task to do the application startup initialization while splash screen is being shown to the user.
     */
    private class StartupTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... params) {
            StartupActivity.this.isUserLoggedIn = UserSessionUtils.isUserLoggedIn(StartupActivity.this);
            CommonUtils.sleep(StartupActivity.this.remainingSleepTimeInMillis());
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (StartupActivity.this.isUserLoggedIn) {
                ActivityUtils.startReadingActivity(StartupActivity.this);
            } else {
                ActivityUtils.startLoginActivity(StartupActivity.this);
            }
        }
    }
}