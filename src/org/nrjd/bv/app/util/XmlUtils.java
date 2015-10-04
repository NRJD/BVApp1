/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XmlUtils {
    private static final Logger LOG = LoggerFactory.getLogger(XmlUtils.class);

    public static DocumentBuilderFactory createDocumentBuilderFactory() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        return factory;
    }

    public static Document parseXml(String xmlData) throws Exception {
        if (xmlData == null) {
            return null;
        }
        // Parse xml data.
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing xml data:\n" +
                    xmlData);
        }
        DocumentBuilderFactory factory = createDocumentBuilderFactory();
        DocumentBuilder builder = null;
        StringReader stringReader = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            stringReader = new StringReader(xmlData);
            InputSource inputSource = new InputSource(stringReader);
            document = builder.parse(inputSource);
        } finally {
            CommonUtils.closeQuietly(stringReader);
        }
        return document;
    }

    public static String getXml(Document inputDoc) throws Exception {
        if (inputDoc == null) {
            return null;
        }
        /*
        // Generate xml from the xml document.
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        StringWriter stringWriter = null;
        String xmlData = null;
        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            DOMSource dom = new DOMSource(inputDoc);
            stringWriter = new StringWriter();
            StreamResult sr = new StreamResult(stringWriter);
            transformer.transform(dom, sr);
            xmlData = stringWriter.toString();
        } finally {
            CommonUtils.closeQuietly(stringWriter);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsed xml data:\n" +
                    xmlData);
        }
        return xmlData;
        */
        return null;
    }

    public static void addElement(Document document, Node node, String elementName, String elementData) {
        if ((document != null) && (node != null) && StringUtils.isNotNullOrEmpty(elementName)) {
            Node childNode = document.createElement(elementName);
            if (StringUtils.isNotNullOrEmpty(elementData)) {
                Node textNode = document.createTextNode(elementData);
                childNode.appendChild(textNode);
            }
            node.appendChild(childNode);
        }
    }

    public static Element findFirstElementByTagName(Document document, String elementName) {
        if ((document != null) && StringUtils.isNotNullOrEmpty(elementName)) {
            NodeList nodeList = document.getElementsByTagName(elementName);
            return getFirstElement(nodeList);
        }
        return null;
    }

    public static Element getFirstChildElementByTagName(Node node, String elementName) {
        if ((node != null) && StringUtils.isNotNullOrEmpty(elementName)) {
            NodeList nodeList = node.getChildNodes();
            return getFirstElementByTagName(nodeList, elementName);
        }
        return null;
    }

    public static List<Element> getChildElementsByTagName(Node node, String elementName) {
        if ((node != null) && StringUtils.isNotNullOrEmpty(elementName)) {
            NodeList nodeList = node.getChildNodes();
            return getElementsByTagName(nodeList, elementName);
        }
        return (List<Element>) null;
    }

    public static String getFirstChildElementValue(Node node, String elementName) {
        Element element = getFirstChildElementByTagName(node, elementName);
        Node firstChildNode = ((element != null) ? element.getFirstChild() : null);
        if ((firstChildNode != null) && (firstChildNode.getNodeType() == Node.TEXT_NODE)) {
            return firstChildNode.getNodeValue();
        }
        return null;
    }

    //////////////////////////////////////// Private Methods ////////////////////////////////////////

    private static List<Element> getElementsByTagName(NodeList nodeList, String elementName) {
        List<Element> elements = null;
        if ((nodeList != null) && (nodeList.getLength() > 0) && StringUtils.isNotNullOrEmpty(elementName)) {
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node childNode = nodeList.item(index);
                if (childNode instanceof Element) {
                    Element childElement = (Element) childNode;
                    if (elementName.equals(childElement.getNodeName())) {
                        if (elements == null) {
                            elements = new ArrayList<Element>();
                        }
                        elements.add(childElement);
                    }
                }
            }
        }
        return elements;
    }

    private static Element getFirstElementByTagName(NodeList nodeList, String elementName) {
        if ((nodeList != null) && (nodeList.getLength() > 0) && StringUtils.isNotNullOrEmpty(elementName)) {
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node childNode = nodeList.item(index);
                if (childNode instanceof Element) {
                    Element childElement = (Element) childNode;
                    if (elementName.equals(childElement.getNodeName())) {
                        return childElement;
                    }
                }
            }
        }
        return null;
    }

    private static Element getFirstElement(NodeList nodeList) {
        Node node = getFirstNode(nodeList);
        if (node instanceof Element) {
            return (Element) node;
        }
        return null;
    }

    private static Node getFirstNode(NodeList nodeList) {
        return (((nodeList != null) && (nodeList.getLength() > 0)) ? nodeList.item(0) : null);
    }
}
