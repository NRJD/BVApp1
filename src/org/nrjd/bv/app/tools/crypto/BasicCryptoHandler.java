/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Internal implementation for CryptoHandler.
 */
class BasicCryptoHandler implements CryptoHandler {
    private static final String ALGORITHM = "AES";
    private static final String PSWD_HASH_ALGORITHM = "SHA-256";
    // Don't store key in String.
    private static final byte[] DEFAULT_KEY = new byte[] { 'e', 'n', 'c', 'r', 'y', 'p', 't', 'i', 'n', 'g', 't', 'h', 'e', 'b', 'v', 'd', 'a', 't', 'a' };
    private byte[] keyData = null;

    public BasicCryptoHandler() {
        this(null);
    }

    public BasicCryptoHandler(byte[] key) {
        this.keyData = key;
    }

    public byte[] encrypt(byte[] data) {
        byte[] encodedBytes = null;
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data);
            encodedBytes = Base64Utils.encode(encryptedBytes);
        } catch (Exception e) {
            // Return same data.
            encodedBytes = data;
        }
        return encodedBytes;
    }

    public byte[] decrypt(byte[] data) {
        byte[] decryptedBytes = null;
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(getDefaultAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = Base64Utils.decode(data);
            decryptedBytes = cipher.doFinal(decodedBytes);
        } catch (Exception e) {
            // Return same data.
            decryptedBytes = data;
        }
        return decryptedBytes;
    }

    private Key generateKey() throws Exception {
        return generateKey16();
        // return generateSHAKey();
    }

    private Key generateKey16() throws Exception {
        byte[] keyBytes = (((keyData != null) && (keyData.length > 0)) ? keyData : DEFAULT_KEY);
        byte[] key16 = new byte[16];
        for (int index = 0; index < key16.length; index++) {
            if ((keyBytes != null) && (keyBytes.length > index)) {
                key16[index] = keyBytes[index];
            } else {
                key16[index] = (byte)('A' + index % 26);
            }
        }
        Key key = new SecretKeySpec(key16, getDefaultAlgorithm());
        return key;
    }

    private Key generateSHAKey() throws Exception {
        byte[] keyBytes = (((keyData != null) && (keyData.length > 0)) ? keyData : DEFAULT_KEY);
        MessageDigest messageDigest = MessageDigest.getInstance(PSWD_HASH_ALGORITHM);
        messageDigest.update(keyBytes);
        byte[] secretKeyData = messageDigest.digest();
        Key key = new SecretKeySpec(secretKeyData, getDefaultAlgorithm());
        return key;
    }

    private static String getDefaultAlgorithm() {
        return ALGORITHM;
    }
}
