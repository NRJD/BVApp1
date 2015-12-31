/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.reg;

import org.nrjd.bv.app.util.XmlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Registry Data utilities.
 * <p>
 * Xml structure:
 * <RegistryData>
 * <BookEntries>
 * <BookEntry><bookName>file 1</bookName><fileName>file1.sepub</fileName><lastModified>2014-11-05 15:05:25</lastModified><size>345</size></BookEntry>
 * </BookEntries>
 * </RegistryData>
 */
public class RegistryDataUtils {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryDataUtils.class);

    private static final String REGISTRY_DATA_TAG = "RegistryData";
    private static final String BOOK_ENTRIES_TAG = "BookEntries";
    private static final String BOOK_ENTRY_TAG = "BookEntry";
    private static final String BOOK_NAME_ATTR = "bookName";
    private static final String FILE_NAME_ATTR = "fileName";
    private static final String LAST_MODIFIED_ATTR = "lastModified";
    private static final String SIZE_ATTR = "size";

    public RegistryDataUtils() {
    }

    public static String generateRegistryXml(RegistryData registryData) throws Exception {
        Document document = constructXmlDocument(registryData);
        String xmlData = XmlUtils.getXml(document);
        return xmlData;
    }

    public static RegistryData parseRegistryXml(String xmlData) throws Exception {
        Document document = XmlUtils.parseXml(xmlData);
        RegistryData registryData = constructRegistryData(document);
        return registryData;
    }

    private static Document constructXmlDocument(RegistryData registryData) throws Exception {
        if (registryData == null) {
            LOG.warn("Could not construct xml document as the given registry data is null. Returning null xml document..");
            return null;
        }
        // Construct xml document from registry data.
        if (LOG.isDebugEnabled()) {
            LOG.debug("Constructing xml document from registry data:\n" +
                    registryData);
        }
        DocumentBuilderFactory factory = XmlUtils.createDocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element registryDataElement = document.createElement(REGISTRY_DATA_TAG);
        document.appendChild(registryDataElement);
        List<BookEntry> bookEntries = registryData.getBookEntries();
        if ((bookEntries != null) && (bookEntries.size() > 0)) {
            Element bookEntriesElement = document.createElement(BOOK_ENTRIES_TAG);
            registryDataElement.appendChild(bookEntriesElement);
            for (BookEntry bookEntry : bookEntries) {
                Element bookEntryElement = document.createElement(BOOK_ENTRY_TAG);
                bookEntriesElement.appendChild(bookEntryElement);
                XmlUtils.addElement(document, bookEntryElement, BOOK_NAME_ATTR, bookEntry.getBookName());
                XmlUtils.addElement(document, bookEntryElement, FILE_NAME_ATTR, bookEntry.getFileName());
                XmlUtils.addElement(document, bookEntryElement, LAST_MODIFIED_ATTR, bookEntry.getLastModified());
                XmlUtils.addElement(document, bookEntryElement, SIZE_ATTR, Long.toString(bookEntry.getSize()));
            }
        }
        return document;
    }

    private static RegistryData constructRegistryData(Document document) throws Exception {
        if (document == null) {
            LOG.warn("Could not construct registry data as the given xml document is null. Returning null registry data..");
            return null;
        }
        // Construct registry data from xml
        RegistryData registryData = null;
        Element registryDataElement = XmlUtils.getFirstChildElementByTagName(document, REGISTRY_DATA_TAG);
        if (registryDataElement != null) {
            registryData = new RegistryData();
            Element bookEntriesElement = XmlUtils.getFirstChildElementByTagName(registryDataElement, BOOK_ENTRIES_TAG);
            List<Element> bookEntryElementList = XmlUtils.getChildElementsByTagName(bookEntriesElement, BOOK_ENTRY_TAG);
            if ((bookEntryElementList != null) && (bookEntryElementList.size() > 0)) {
                for (Element bookEntryElement : bookEntryElementList) {
                    BookEntry bookEntry = new BookEntry();
                    bookEntry.setBookName(XmlUtils.getFirstChildElementValue(bookEntryElement, BOOK_NAME_ATTR));
                    bookEntry.setFileName(XmlUtils.getFirstChildElementValue(bookEntryElement, FILE_NAME_ATTR));
                    bookEntry.setLastModified(XmlUtils.getFirstChildElementValue(bookEntryElement, LAST_MODIFIED_ATTR));
                    bookEntry.setSize(parseLong(XmlUtils.getFirstChildElementValue(bookEntryElement, SIZE_ATTR)));
                    registryData.addBookEntry(bookEntry);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Constructed registry data:\n" +
                    registryData);
        }
        return registryData;
    }

    private static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return -1;
        }
    }
}

