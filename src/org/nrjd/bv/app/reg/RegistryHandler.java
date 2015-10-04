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
    // BVAppData1 folder
    private static final String NET_FOLDER = "https://googledrive.com/host/0B1vAOgyjy-9lZ0dPNURjODdBSVk";
    private static final String REGISTRY_FILE_NAME = "registry1.xml";

    // Data1 folder
    private static final String NET_FOLDER1 = "https://googledrive.com/host/0B1vAOgyjy-9lcDh4RHNGcFBwems";
    private static final List<String> BOOK_NAMES1 = Arrays.<String>asList("A1.epub", "G1.epub", "M1.epub");

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
