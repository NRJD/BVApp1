/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;


public class CommonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    public static void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e = null; // Ignore the exception.
            }
        }
    }

    public static void sleep(String message, long sleepSecs) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(message + ": Sleeping for " + sleepSecs + "..");
            }
            Thread.currentThread().sleep(sleepSecs * 1000);
        } catch (Exception e) {
            e = null; // Ignore the exception.
        }
    }
}
