/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private FileUtils() {
    }

    /**
     * Retrieves the data from the given file.
     *
     * @param file file to be read.
     * @return data retrieved from the given file.
     */
    public static String getFileData(File file) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading data from file: " + file + "..");
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder buffer = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, AppConstants.UTF8);
            br = new BufferedReader(isr);
            char[] readBuffer = new char[4098];
            int length = 0;
            while ((length = br.read(readBuffer)) > 0) {
                buffer.append(readBuffer, 0, length);
            }
        } catch (Exception e) {
            // Ignore the exception.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to retrieve data from: " + file + ": " + e.getMessage());
            }
        } finally {
            CommonUtils.closeQuietly(br);
            CommonUtils.closeQuietly(isr);
            CommonUtils.closeQuietly(fis);
        }
        return buffer.toString();
    }
}