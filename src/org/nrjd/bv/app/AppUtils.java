/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app;

import net.nightwhistler.pageturner.library.LibraryService;


public class AppUtils {
    public static boolean isEmptyLibrary(LibraryService libraryService) {
        return libraryService.findAllByTitle(null).getSize() < 1;
    }

    public static void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}