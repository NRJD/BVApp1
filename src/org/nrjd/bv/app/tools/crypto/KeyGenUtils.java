/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import java.util.Random;
import java.util.UUID;

import org.nrjd.bv.app.util.AppConstants;


/**
 * Internal random key generation utilities.
 */
class KeyGenUtils {
    private static final int DEFAULT_KEY_LEN = 16;
    private static final char UUID_SEPERATOR = '-';
    private static final String UTF8 = AppConstants.UTF8;
    private static final int MASK_CHARS_PADDING_PERCENT = 20;
    private static final int MASK_CHARS_UPPER_ASCII_LIMIT = 30;
    private static final Random randomMaskChar = new Random();

    private KeyGenUtils() {
    }

    static String getCharSet() {
        return UTF8;
    }

    static byte[] generateKey(int keyLength) {
        keyLength = ((keyLength > 0) ? keyLength : DEFAULT_KEY_LEN);
        byte[] key = new byte[keyLength];
        int currentFillPosition = 0;
        while (currentFillPosition < keyLength) {
            String uuid = UUID.randomUUID().toString();
            for (int index = 0; index < uuid.length(); index++) {
                if ((currentFillPosition < keyLength) && (uuid.charAt(index) != UUID_SEPERATOR)) {
                    key[currentFillPosition] = (byte)uuid.charAt(index);
                    currentFillPosition++;
                }
            }
        }
        return key;
    }

    static int generateRandomMarkCharValue() {
        int value = ((randomMaskChar.nextInt() % MASK_CHARS_UPPER_ASCII_LIMIT) + 1);
        return Math.abs(value);
    }

    static int generateRandomMarkCharIndex(int paddingLength) {
        int index = (generateRandomMarkCharValue() % paddingLength);
        return index;
    }

    static byte generateRandomMarkCharByte() {
        int ch = ((randomMaskChar.nextInt() % MASK_CHARS_UPPER_ASCII_LIMIT) + 1);
        return (byte)ch;
    }

    static byte[] generatePadding(int paddingLength) {
        byte[] padding = generateKey(paddingLength);
        // Add mask chars.
        int numberOfMaskChars = ((paddingLength * MASK_CHARS_PADDING_PERCENT) / 100);
        for (int index = 0; index < numberOfMaskChars; index++) {
            int markCharIndex = generateRandomMarkCharIndex(paddingLength);
            byte markCharByte = generateRandomMarkCharByte();
            padding[markCharIndex] = markCharByte;
        }
        return padding;
    }

    static void copyBytes(byte[] source, byte[] destination, int destinationStartIndex) {
        copyBytes(source, 0, source.length, destination, destinationStartIndex);
    }

    static void copyBytes(byte[] source, int sourceStartIndex, int dataLength, byte[] destination, int destinationStartIndex) {
        if ((source != null) && (destination != null)) {
            int sourceIndex = sourceStartIndex;
            int sourceEndIndexInclusive = sourceStartIndex + dataLength - 1;
            int destinationIndex = destinationStartIndex;
            while ((sourceIndex <= sourceEndIndexInclusive) && (sourceIndex < source.length) && (destinationIndex < destination.length)) {
                destination[destinationIndex] = source[sourceIndex];
                sourceIndex++;
                destinationIndex++;
            }
        }
    }
}
