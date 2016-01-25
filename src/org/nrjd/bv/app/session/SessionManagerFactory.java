/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.session;

import android.content.Context;


/**
 * Session manager factory.
 */
public class SessionManagerFactory {
    private SessionManagerFactory() {
    }

    public static UserSessionManager getUserSessionManager(Context context) {
        return new UserSessionManager(context);
    }
}