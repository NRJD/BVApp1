/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.reg;

import java.util.ArrayList;
import java.util.List;


/**
 * Registry Data.
 */
public class RegistryData {
    private List<BookEntry> bookEntries = new ArrayList<BookEntry>();

    public RegistryData() {
    }

    public List<BookEntry> getBookEntries() {
        return this.bookEntries;
    }

    public void setBookEntries(List<BookEntry> bookEntries) {
        this.bookEntries = new ArrayList<BookEntry>();
        addBookEntries(bookEntries);
    }

    public void addBookEntry(BookEntry bookEntry) {
        if (bookEntry != null) {
            this.bookEntries.add(bookEntry);
        }
    }

    public void addBookEntries(List<BookEntry> bookEntries) {
        if (bookEntries != null) {
            for (BookEntry bookEntry : bookEntries) {
                addBookEntry(bookEntry);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(256);
        toString(buffer);
        return buffer.toString();
    }

    void toString(StringBuilder buffer) {
        if (buffer != null) {
            buffer.append(this.getClass().getSimpleName());
            buffer.append("[book-entries: [size=");
            buffer.append(bookEntries.size());
            if (bookEntries.size() > 0) {
                int index = 1;
                for (BookEntry bookEntry : bookEntries) {
                    buffer.append(", ").append(index++).append(": ");
                    bookEntry.toString(buffer);
                }
            }
            buffer.append("]]");
        }
    }
}
