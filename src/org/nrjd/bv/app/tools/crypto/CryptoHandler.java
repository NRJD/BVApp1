/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;


public interface CryptoHandler {
    public byte[] encrypt(byte[] data);

    public byte[] decrypt(byte[] data);
}
