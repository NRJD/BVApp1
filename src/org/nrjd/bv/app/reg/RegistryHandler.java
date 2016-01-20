/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.reg;

import java.util.Arrays;
import java.util.List;


/**
 * Registry Handler.
 */
public class RegistryHandler {
    private String baseUrl = null;
    private String registryFileName = null;

    public RegistryHandler(String baseUrl, String registryFileName) {
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
