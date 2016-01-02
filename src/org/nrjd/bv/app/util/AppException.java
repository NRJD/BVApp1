/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.util;

/**
 * Data service provider.
 */
public class AppException extends Exception {
    private static final long serialVersionUID = 1L;
    private ErrorCode errorCode = null;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }

    public AppException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AppException(String message, Throwable throwable, ErrorCode errorCode) {
        this(message, throwable);
        this.errorCode = errorCode;
    }

    public AppException(Throwable throwable, ErrorCode errorCode) {
        this(throwable);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}