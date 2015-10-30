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
    // TestData2 folder
    private static final String NET_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lZ0dPNURjODdBSVk";
    private static final String REGISTRY_FILE_NAME = "registry2.xml";

    // MiniBooks1 folder
    private static final String NET_FOLDER3 = "https://googledrive.com/host/0B1vAOgyjy-9lU3hnVUlmanJMckE";
    private static final String REGISTRY_FILE_NAME3 = "registry21.xml";

    // TestData1 folder
    private static final String NET_FOLDER2 = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";

    private String baseUrl = null;
    private String registryFileName = null;

    public RegistryHandler() {
        // this(NET_FOLDER, REGISTRY_FILE_NAME);
        this(NET_FOLDER3, REGISTRY_FILE_NAME3);
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
