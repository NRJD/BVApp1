/*
 * Copyright (C) 2016 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.reg;

import net.nightwhistler.pageturner.library.LibraryBook;

import org.nrjd.bv.app.util.StringUtils;


public class BookDataUtils {
    private static final String BV = "BV".toLowerCase();
    private static final String BHAKTI = "Bhakti".toLowerCase();
    private static final String SHRADDHAVAN = "Shraddhavan".toLowerCase();

    private BookDataUtils() {
    }

    public static boolean isContentAvailable(LibraryBook libraryBook) {
        String bookTitle = ((libraryBook != null) ? libraryBook.getTitle() : null);
        String fileName = ((libraryBook != null) ? libraryBook.getFileName() : null);
        if (StringUtils.isNotNullOrEmpty(bookTitle) && StringUtils.isNotNullOrEmpty(fileName)) {
            boolean isBVBook = isBVBook(bookTitle) || isBVBook(fileName);
            boolean isShraddhavanBook = isShraddhavanBook(bookTitle) || isShraddhavanBook(fileName);
            if (isBVBook && (!isShraddhavanBook)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBVBook(String name) {
        if (name != null) {
            name = name.toLowerCase();
            return (name.contains(BV) || name.contains(BHAKTI));
        }
        return false;
    }

    private static boolean isShraddhavanBook(String name) {
        if (name != null) {
            name = name.toLowerCase();
            return name.contains(SHRADDHAVAN);
        }
        return false;
    }
}

