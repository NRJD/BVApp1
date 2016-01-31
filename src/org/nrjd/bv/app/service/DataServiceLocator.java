/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.reg.RegistryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data service locator.
 */
public class DataServiceLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataServiceLocator.class);

    // Server URL constants
    // private static final String BASE_SERVER_URL = "http://52.32.43.240:8011/BVServer";
    private static final String BASE_SERVER_URL = "http://10.0.0.2:8011/BVServer";
    private static final String SERVER_DATA_URL = BASE_SERVER_URL + "/mobileReq";

    // Test data folders.
    private static final String MINI_BOOKS_1_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lU3hnVUlmanJMckE";
    private static final String TEST_DATA_1_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";
    private static final String TEST_DATA_2_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lZ0dPNURjODdBSVk";
    private static final String TEST_DATA_3_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lbTFkVnl0a0lfZjA";
    private static final String DEMO_BOOKS_1_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lWEpkdDhEbFJOUzQ";
    private static final String DEMO_BOOKS_2_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lV3lKdmV6LXFaOUE";

    //// Net download data.
    private static final String NET_FOLDER = DEMO_BOOKS_2_FOLDER;
    private static final String REGISTRY_FILE_NAME = "registry1.xml";

    private DataServiceLocator() {
    }

    public static String getServerDataUrl() {
        return SERVER_DATA_URL;
    }

    public static RegistryHandler getRegistryHandler() {
        return new RegistryHandler(NET_FOLDER, REGISTRY_FILE_NAME);
    }
}