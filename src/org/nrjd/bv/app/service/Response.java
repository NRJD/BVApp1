/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.util.ErrorCode;
import org.nrjd.bv.app.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Response data.
 */
public class Response {
    private boolean isSuccess = true;
    private ErrorCode errorCode = null;
    private Map<String, Object> parameters = new HashMap<String, Object>();

    private Response(boolean isSuccess, ErrorCode errorCode) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
    }

    public static Response createSuccessResponse() {
        return new Response(true, null);
    }

    public static Response createFailedResponse(ErrorCode errorCode) {
        return new Response(false, errorCode);
    }

    public static Response getServiceErrorResponse() {
        return createFailedResponse(ErrorCode.EC_SERVICE_ERROR_OCCURRED);
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public static ErrorCode getErrorCodeOrGenericError(Response response) {
        ErrorCode error = ((response != null) ? response.getErrorCode() : null);
        return ((error != null) ? error : ErrorCode.getGenericErrorCode());
    }

    public Object getParameter(String name) {
        return (StringUtils.isNotNullOrEmpty(name) ? this.parameters.get(name) : null);
    }

    public void addParameter(String name, Object value) {
        if (StringUtils.isNotNullOrEmpty(name)) {
            if (value != null) {
                this.parameters.put(name, value);
            } else {
                removeParameter(name);
            }
        }
    }

    public Object removeParameter(String name) {
        return (StringUtils.isNotNullOrEmpty(name) ?this.parameters.remove(name) : null);
    }
}