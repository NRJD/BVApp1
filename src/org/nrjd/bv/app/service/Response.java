/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.service;

public class Response {
    private boolean isSuccess = true;
    private ErrorCode errorCode = null;

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
}