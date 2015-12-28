/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import android.util.Base64;

/**
 * Internal Base64 utilities.
 */
class Base64Utils {
    private Base64Utils() {
    }

    public static byte[] encode(byte[] data) {
        byte[] encodedBytes = null;
        try {
            encodedBytes = Base64.encode(data, 0);
        } catch (Exception e) {
            encodedBytes = null;
        }
        return encodedBytes;
    }

    public static byte[] decode(byte[] data) {
        byte[] decodedBytes = null;
        try {
            decodedBytes = Base64.decode(data, 0);
        } catch (Exception e) {
            decodedBytes = null;
        }
        return decodedBytes;
    }
}
