/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import org.nrjd.bv.app.util.StringUtils;


/**
 * Internal class to hold algo schema.
 */
class AlgoSchema {
    // Algo schema mask bytes.
    private static byte ALGO_SCHEMA_MASK_BYTE_1 = 1;
    private static byte ALGO_SCHEMA_MASK_BYTE_2 = 2;
    private static byte[] ALGO_SCHEMA_MASK_BYTES = new byte[] { ALGO_SCHEMA_MASK_BYTE_1, ALGO_SCHEMA_MASK_BYTE_2 };
    // Algo schema mask byte separator byte sequence.
    private static byte ALGO_SCHEMA_SEPARATOR_BYTE_1 = 1;
    private static byte ALGO_SCHEMA_SEPARATOR_BYTE_2 = 2;
    private static byte ALGO_SCHEMA_SEPARATOR_BYTE_3 = 3;
    private static byte ALGO_SCHEMA_SEPARATOR_BYTE_4 = 4;
    private static byte ALGO_SCHEMA_SEPARATOR_BYTE_5 = 5;
    private static int ALGO_SCHEMA_SEPARATOR_START_MARKER = 1;
    private static int ALGO_SCHEMA_SEPARATOR_END_MARKER = 5;
    private static int ALGO_SCHEMA_SEPARATOR_SAFE_END_MARKER = ALGO_SCHEMA_SEPARATOR_END_MARKER + 3;
    // Algo schema properties.
    private static final String PROP_CRYPTO_ALGO_SCHEMA_ID = "schema";
    private static final String PROP_CRYPTO_RELEASE_VER = "ver";
    private static final String PROPERTY_VALUE_SEPERATOR = "=";
    private static final String PROPERTY_SEPERATOR = ";";
    private static final int NONE_VALUE = 0;
    private int schemaId = NONE_VALUE;
    private int releaseVersion = NONE_VALUE;

    AlgoSchema() {
    }

    AlgoSchema(int schemaId, int releaseVersion) {
        this.schemaId = schemaId;
        this.releaseVersion = releaseVersion;
    }

    public int getSchemaId() {
        return this.schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public int getReleaseVersion() {
        return this.releaseVersion;
    }

    public void setReleaseVersion(int releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public static byte[] generateAlgoSchemaBytes(AlgoSchema algoSchema, int algoSchemaBytesLength) {
        byte[] algoSchemaBytes = new byte[algoSchemaBytesLength];
        // Fill algo schema bytes with random padding data.
        byte[] randomPaddingData = KeyGenUtils.generatePadding(algoSchemaBytesLength);
        KeyGenUtils.copyBytes(randomPaddingData, algoSchemaBytes, 0);
        // Add algo schema data.
        int algoSchemaDataSeparatorIndex = 0;
        if (algoSchema != null) {
            StringBuilder buffer = new StringBuilder(algoSchemaBytesLength);
            buffer.append(PROP_CRYPTO_ALGO_SCHEMA_ID).append(PROPERTY_VALUE_SEPERATOR).append(algoSchema.getSchemaId());
            buffer.append(PROPERTY_SEPERATOR).append(PROP_CRYPTO_RELEASE_VER).append(PROPERTY_VALUE_SEPERATOR).append(algoSchema.getReleaseVersion());
            byte[] propertyBytes = null;
            try {
                propertyBytes = buffer.toString().getBytes(KeyGenUtils.getCharSet());
            } catch (Exception e) {
                propertyBytes = null;
            }
            // Copy algo schema properties data.
            if (propertyBytes != null) {
                KeyGenUtils.copyBytes(propertyBytes, algoSchemaBytes, 0);
                algoSchemaDataSeparatorIndex = propertyBytes.length;
            }
        }
        // Add algo schema data separator marker.
        for (int byteValue = ALGO_SCHEMA_SEPARATOR_START_MARKER;
             ((byteValue <= ALGO_SCHEMA_SEPARATOR_SAFE_END_MARKER) && (algoSchemaDataSeparatorIndex < algoSchemaBytes.length)); byteValue++) {
            algoSchemaBytes[algoSchemaDataSeparatorIndex] = (byte)byteValue;
            algoSchemaDataSeparatorIndex++;
        }
        return algoSchemaBytes;
    }

    public static AlgoSchema parseAlgoSchemaFromBytes(byte[] algoSchemaBytes) {
        AlgoSchema algoSchema = null;
        if ((algoSchemaBytes != null) && (algoSchemaBytes.length > 0)) {
            // Trim algo schema data at separator marker.
            int algoSchemaDataSeparatorIndex = 0;
            for (int index = 0; index < algoSchemaBytes.length; index++) {
                if (isAlgoSchemaDataSeparator(algoSchemaBytes, index)) {
                    algoSchemaDataSeparatorIndex = index;
                    break; // Break from here once algoSchemaDataSeparatorIndex obtained.
                }
            }
            try {
                String algoSchemaString = new String(algoSchemaBytes, 0, algoSchemaDataSeparatorIndex, KeyGenUtils.getCharSet());
                algoSchema = parseAlgoSchema(algoSchemaString);
            } catch (Exception e) {
                algoSchema = null;
            }
        }
        return algoSchema;
    }

    private static boolean isAlgoSchemaDataSeparator(byte[] algoSchemaBytes, int index) {
        int startSepartorIndex = index;
        int endSepartorIndex = index + 4;
        if ((algoSchemaBytes != null) && (algoSchemaBytes[startSepartorIndex] == ALGO_SCHEMA_SEPARATOR_BYTE_1) &&
            (endSepartorIndex < algoSchemaBytes.length)) {
            return ((algoSchemaBytes[startSepartorIndex] == ALGO_SCHEMA_SEPARATOR_BYTE_1) &&
                    (algoSchemaBytes[startSepartorIndex + 1] == ALGO_SCHEMA_SEPARATOR_BYTE_2) &&
                    (algoSchemaBytes[startSepartorIndex + 2] == ALGO_SCHEMA_SEPARATOR_BYTE_3) &&
                    (algoSchemaBytes[startSepartorIndex + 3] == ALGO_SCHEMA_SEPARATOR_BYTE_4) &&
                    (algoSchemaBytes[startSepartorIndex + 4] == ALGO_SCHEMA_SEPARATOR_BYTE_5));
        }
        return false;
    }

    private static void fillAlgoSchemaMaskBytes(byte[] algoSchemaBytes) {
        if (algoSchemaBytes != null) {
            for (int index = 0; index < algoSchemaBytes.length; index++) {
                algoSchemaBytes[index] = ALGO_SCHEMA_MASK_BYTES[index % ALGO_SCHEMA_MASK_BYTES.length];
            }
        }
    }

    private static boolean isAlgoSchemaMarkByte(byte algoSchemaByte) {
        return ((algoSchemaByte == ALGO_SCHEMA_MASK_BYTE_1) || (algoSchemaByte == ALGO_SCHEMA_MASK_BYTE_2));
    }

    private static AlgoSchema parseAlgoSchema(String algoSchemaString) {
        AlgoSchema algoSchema = null;
        if (algoSchemaString != null) {
            algoSchemaString = algoSchemaString.trim();
            String[] properties = algoSchemaString.split(PROPERTY_SEPERATOR);
            if (properties != null) {
                for (int index = 0; index < properties.length; index++) {
                    String property = properties[index];
                    algoSchema = parseAlgoSchemaproperty(algoSchema, property);
                }
            }
        }
        return algoSchema;
    }

    private static AlgoSchema parseAlgoSchemaproperty(AlgoSchema algoSchema, String property) {
        if (property != null) {
            property = property.trim();
            String[] items = property.split(PROPERTY_VALUE_SEPERATOR);
            if (items != null) {
                String propertyName = ((items.length >= 2) ? items[0] : null);
                String propertyValue = ((items.length >= 2) ? items[1] : null);
                algoSchema = parseAlgoSchemaproperty(algoSchema, propertyName, propertyValue);
            }
        }
        return algoSchema;
    }

    private static AlgoSchema parseAlgoSchemaproperty(AlgoSchema algoSchema, String propertyName, String propertyValue) {
        if (StringUtils.isNotNullOrEmpty(propertyName) && StringUtils.isNotNullOrEmpty(propertyValue)) {
            propertyName = propertyName.trim();
            propertyValue = propertyValue.trim();
            if (PROP_CRYPTO_ALGO_SCHEMA_ID.equals(propertyName)) {
                if (algoSchema == null) {
                    algoSchema = new AlgoSchema();
                }
                algoSchema.setSchemaId(parseInt(propertyValue, NONE_VALUE));
            } else if (PROP_CRYPTO_RELEASE_VER.equals(propertyName)) {
                if (algoSchema == null) {
                    algoSchema = new AlgoSchema();
                }
                algoSchema.setReleaseVersion(parseInt(propertyValue, NONE_VALUE));
            }
        }
        return algoSchema;
    }

    private static int parseInt(String propertyValue, int defaultValue) {
        try {
            return Integer.parseInt(propertyValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String toString() {
        return "AlgoSchema[" + PROP_CRYPTO_ALGO_SCHEMA_ID + "=" + getSchemaId() + ", " + PROP_CRYPTO_RELEASE_VER + "=" + getReleaseVersion() + "]";
    }
}
