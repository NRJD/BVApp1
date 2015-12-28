/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.reg;

import java.util.Arrays;
import java.util.List;


/**
 * Application constants.
 */
public class RegistryHandler {
    // Test data folders.
    private static final String MINI_BOOKS_1_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lU3hnVUlmanJMckE";
    private static final String TEST_DATA_1_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";
    private static final String TEST_DATA_2_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lZ0dPNURjODdBSVk";
    private static final String TEST_DATA_3_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lbTFkVnl0a0lfZjA";

    //// Net download data.
    private static final String NET_FOLDER = TEST_DATA_3_FOLDER;
    private static final String REGISTRY_FILE_NAME = "registry1.xml";

    private String baseUrl = null;
    private String registryFileName = null;

    public RegistryHandler() {
        this(NET_FOLDER, REGISTRY_FILE_NAME);
    }

    private RegistryHandler(String baseUrl, String registryFileName) {
        this.baseUrl = baseUrl;
        this.registryFileName = registryFileName;
    }

    public String getRegistryUrl() {
        return this.baseUrl + "/" + this.registryFileName;
    }
    public String getBookUrl(String fileName) {
        return this.baseUrl + "/" + fileName;
    }
}
