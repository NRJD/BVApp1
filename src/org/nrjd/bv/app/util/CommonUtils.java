/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;


public class CommonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private CommonUtils() {
    }

    public static void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e = null; // Ignore the exception.
            }
        }
    }

    public static void sleep(int milliSecs) {
        sleep(null, milliSecs);
    }

    public static void sleep(String message, long milliSecs) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(message + ": Sleeping for " + milliSecs + " ms..");
            }
            Thread.currentThread().sleep(milliSecs);
        } catch (Exception e) {
            e = null; // Ignore the exception.
        }
    }
}
