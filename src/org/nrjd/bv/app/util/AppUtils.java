/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import net.nightwhistler.pageturner.library.LibraryService;


public class AppUtils {
    /**
     * Private constructor to prevent the instantiation of this class.
     */
    private AppUtils() {
    }

    public static boolean isEmptyLibrary(LibraryService libraryService) {
        return libraryService.findAllByTitle(null).getSize() < 1;
    }
}