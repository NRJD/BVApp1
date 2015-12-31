/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;


public class CryptoHandlerFactory {

    private CryptoHandlerFactory() {
    }

    public static CryptoHandler getInstance() {
        // return new BasicCryptoHandler();
        return new CryptoHandlerImpl();
    }
}
