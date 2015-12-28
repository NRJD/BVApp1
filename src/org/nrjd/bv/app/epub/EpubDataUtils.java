/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.epub;

import org.nrjd.bv.app.tools.crypto.CryptoHandlerFactory;
import org.nrjd.bv.app.util.AppConstants;

import java.util.Locale;

import nl.siegmann.epublib.domain.Resource;


public class EpubDataUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private EpubDataUtils() {
    }

    public static boolean isValidBookName(String fileName) {
        if (fileName != null) {
            String fileNameLower = fileName.toLowerCase(Locale.US);
            return (fileNameLower.endsWith(AppConstants.BOOK_EXTENSION) || fileNameLower.endsWith(AppConstants.BOOK_EXTENSION_2));
        }
        return false;
    }

    public static String appendBookExtension(String fileName) {
        return fileName + AppConstants.BOOK_EXTENSION;
    }

    public static void decryptResourceData(Resource resource) {
        if (resource != null) {
            try {
                byte[] data = resource.getData();
                byte[] decryptedData = CryptoHandlerFactory.getInstance().decrypt(data);
                resource.setData(decryptedData);
            } catch (Exception e) {
                e = null; // Ignore the exception.
            }
        }
    }
}