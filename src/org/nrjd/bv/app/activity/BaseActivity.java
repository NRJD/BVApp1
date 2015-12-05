/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.activity;

import android.app.Activity;
import android.widget.Toast;

/**
 * Base activity class.
 */
public abstract class BaseActivity extends Activity {
    private Toast currentToastView = null;

    /**
     * This method indicates whether to retain this activity in the back button history.
     * <p>
     * @return {@code true} If this activity should be retained in the back button history, that is,
     * this activity should be shown to the user when the user presses the back button.
     * Otherwise returns {@code false}.
     */
    abstract public boolean retainActivityInBackButtonHistory();

    /**
     * Cancels the current toast message view that is being shown in this activity.
     */
    protected void cancelToastMessage() {
        // Cancel the current toast message view.
        // TODO: If toast message is already done showing, then no needed to cancel it.
        if (this.currentToastView != null) {
            currentToastView.cancel();
        }
    }

    /**
     * Shows the given toast message in this activity.
     */
    protected void showToastMessage(String message, int duration) {
        showToastMessage(Toast.makeText(getBaseContext(), message, duration));
    }

    /**
     * Shows the given toast message view in this activity.
     */
    protected void showToastMessage(Toast toastView) {
        // If already a toast message exists for this activity, first cancel that.
        cancelToastMessage();
        // Show the given toast message.
        if (toastView != null) {
            this.currentToastView = toastView;
            toastView.show();
        }
    }
}