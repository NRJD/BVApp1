/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;

import org.apache.http.conn.HttpHostConnectException;
import org.nrjd.bv.app.util.ErrorCode;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;

/**
 * Data service utils.
 */
public class DataServiceUtils {
    private DataServiceUtils() {
    }

    public static Response getDataServiceErrorResponse(DataServiceException e) {
        ErrorCode errorCode = ((e != null) ? e.getErrorCode() : null);
        Response response = ((errorCode != null) ? Response.createFailedResponse(errorCode) : null);
        return response;
    }

    public static boolean containsTimeoutExceptionDuringRead(Throwable t) {
        while (t != null) {
            if (isInterruptedIOException(t)) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    public static boolean containsConnectionException(Throwable t) {
        while (t != null) {
            if (isConnectionException(t)) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static boolean isInterruptedIOException(Throwable t) {
        return ((t != null) && (t instanceof InterruptedIOException));
    }


    private static boolean isConnectionException(Throwable t) {
        return ((t != null) &&
                ((t instanceof ConnectException) || (t instanceof IOException)
                        || (t instanceof SocketException) || (t instanceof HttpHostConnectException)
                        || (t.getClass().getPackage().getName().equalsIgnoreCase("java.net"))));
    }
}