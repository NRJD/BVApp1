/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


import org.json.JSONObject;
import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data service provider.
 */
public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    public static String generateJsonData(JSONObject jsonObject) {
        String jsonData = ((jsonObject != null) ? jsonObject.toString() : null);
        return jsonData;
    }

    public static JSONObject parseJsonData(String data) {
        JSONObject jsonObject = null;
        if (StringUtils.isNotNullOrEmpty(data)) {
            data = data.trim();
            try {
                jsonObject = new JSONObject(data);
            } catch (Exception e) {
                jsonObject = null;
                // Ignore exception.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Error parsing the JSON data", e);
                }
            }
        }
        return jsonObject;
    }

    public static String getJsonParameter(JSONObject jsonObject, String name) {
        String value = null;
        if ((jsonObject != null) && StringUtils.isNotNullOrEmpty(name)) {
            try {
                Object object = jsonObject.get(name);
                value = ((object != null)? object.toString() : null);
            } catch (Exception e) {
                // Ignore exception.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Error adding the JSON parameter: " + name, e);
                }
            }
        }
        return value;
    }

    public static void addJsonParameter(JSONObject jsonObject, String name, String value) {
        if ((jsonObject != null) && StringUtils.isNotNullOrEmpty(name) && StringUtils.isNotNullOrEmpty(value)) {
            try {
                jsonObject.put(name, value);
            } catch (Exception e) {
                // Ignore exception.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Error adding the JSON parameter: " + name, e);
                }
            }
        }
    }
}