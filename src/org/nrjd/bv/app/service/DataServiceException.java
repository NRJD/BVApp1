/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.nrjd.bv.app.util.AppException;
import org.nrjd.bv.app.util.ErrorCode;


/**
 * Data service exception.
 */
public class DataServiceException extends AppException {
    private static final long serialVersionUID = 1L;

    public DataServiceException() {
    }

    public DataServiceException(String message) {
        super(message);
    }

    public DataServiceException(Throwable throwable) {
        super(throwable);
    }

    public DataServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DataServiceException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    public DataServiceException(Throwable throwable, ErrorCode errorCode) {
        super(throwable, errorCode);
    }
}