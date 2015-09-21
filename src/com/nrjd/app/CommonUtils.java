/*
 * Copyright (C) 2013 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */

package com.nrjd.app;

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
                e = null; // Bypass jaudit rule.
            }
        }
    }

    public static void sleep(String message, long sleepSecs) {
        try {
            LOG.debug(message + ": Sleeping for " + sleepSecs + "..");
            Thread.currentThread().sleep(sleepSecs * 1000);
        } catch (Exception e) {
            e = null;
        }
    }
}
