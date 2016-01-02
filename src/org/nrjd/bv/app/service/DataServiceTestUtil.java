/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


import net.nightwhistler.pageturner.PageTurnerModule;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.nrjd.bv.app.ctx.AppContext;
import org.nrjd.bv.app.util.AppConstants;
import org.nrjd.bv.app.util.CommonUtils;
import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Data service connection test utility.
 */
public class DataServiceTestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DataServiceTestUtil.class);
    private AppContext appContext = null;
    private HttpClient httpClient;

    public DataServiceTestUtil(AppContext appContext) {
        this.appContext = appContext;
        this.httpClient = PageTurnerModule.getSSLHttpClient();
    }

    public void performHttpTest() {
        String registryXml = "https://googledrive.com/host/0B1vAOgyjy-9lbTFkVnl0a0lfZjA/registry1.xml";
        String bookUrl = "https://googledrive.com/host/0B1vAOgyjy-9lbTFkVnl0a0lfZjA/B21.bk";
        String basePublicUrl = "http://52.32.43.240:8011/BVServer";
        String baseLocalUrl = "http://10.0.0.2:8011/BVServer";
        String getUrl = baseLocalUrl;
        String registerUrl = baseLocalUrl + "/mobileReq";
        performRegister(registerUrl, "user@bvtest.com");
        performGet(getUrl, false);
        performGet(registryXml, false);
        performGet(bookUrl, true);
    }

    private void performGet(String url, boolean isDownloadFile) {
        performAction(url, false, isDownloadFile, null);
    }

    public void performRegister(String url, String userId) {
        performAction(url, true, false, userId);
    }

    private void performAction(String url, boolean isUserRegistration, boolean isDownloadFile, String userId) {
        try {
            HttpUriRequest request = null;
            if (isUserRegistration) {
                LOG.info("Performing HTTP POST: " + url);
                HttpPost post = new HttpPost(url);
                String postData = "{\"cmd\":\"register\",\"name\":\"Test1\",\"password\":\"123\",\"email\":\"" + userId + "\",\"phoneNum\":\"1234567890\",\"language\":\"Eng\", \"countryCode\" : \"1\"}";
                StringEntity stringEntity = new StringEntity(postData);
                post.setEntity(stringEntity);
                request = post;
            } else {
                LOG.info("Performing HTTP GET: " + url);
                request = new HttpGet(url);
            }
            HttpResponse response = this.httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                readData(url, response, isUserRegistration, isDownloadFile);
            } else {
                // TODO: Don't show internal exception messages to end users.
                LOG.error(url + ": Error" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            // TODO: Don't show internal exception messages to end users.
            LOG.error(url + ": Error occurred", e);
        }
    }

    private void readData(String url, HttpResponse response, boolean isPostRequest, boolean isDownloadFile) throws Exception {
        // File size is used for calculating the download progress.
        // If we don't get proper content-length in http response, then assign it from book entry registry.
        HttpEntity httpEntity = response.getEntity();
        long dataSize = httpEntity.getContentLength();
        if ((dataSize < 0)) {
            LOG.warn("Got invalid content-length: " + dataSize);
            dataSize = 0;
        }
        LOG.info("Got content-length: " + dataSize);
        InputStream inputStream = httpEntity.getContent();
        if(isDownloadFile) {
            File downloadTestFolder = ((this.appContext != null)? this.appContext.getDownloadsTestFolder() : null);
            if(downloadTestFolder != null) {
                downloadTestFolder.mkdirs();
                String downloadFileName = (((url != null) && (url.lastIndexOf("/") >= 0))? url.substring(url.lastIndexOf("/")+1) : null);
                downloadFileName = (StringUtils.isNotNullOrEmpty(downloadFileName)? downloadFileName : "Test.bk");
                File downloadFile = new File(downloadTestFolder, downloadFileName);
                saveFile(inputStream, downloadFile);
                LOG.debug("Got File: " + downloadFile + ": " + downloadFile.length() + " bytes");
            }
        } else {
            String data = readData(inputStream);
            LOG.debug("Got Data:\n" + data + "\n<<");
            LOG.debug("Got Data (new lines removed):\n" + data.replaceAll("\n", "").replaceAll("\r", "") + "\n<<");
        }
    }

    private static String readData(InputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        long downloadedSize = 0;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, AppConstants.UTF8);
            reader = new BufferedReader(inputStreamReader);
            char[] data = new char[4098];
            int readSize = 0;
            // TODO: Handle cancel event while processing the request.
            while ((readSize = reader.read(data)) > 0) {
                buffer.append(data, 0, readSize);
                downloadedSize += readSize;
            }
        } finally {
            CommonUtils.closeQuietly(reader);
            CommonUtils.closeQuietly(inputStreamReader);
            CommonUtils.closeQuietly(inputStream);
        }
        String data = buffer.toString();
        LOG.info("Got Data Size: " + downloadedSize);
        return data;
    }

    private static void saveFile(InputStream inputStream, File file) throws IOException {
        long downloadedSize = 0;
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] data = new byte[4098];
            int readSize = 0;
            // TODO: Handle cancel event while processing the request.
            while ((readSize = bufferedInputStream.read(data)) > 0) {
                fos.write(data, 0, readSize);
                downloadedSize += readSize;
            }
        } finally {
            CommonUtils.closeQuietly(bufferedInputStream);
            CommonUtils.closeQuietly(inputStream);
        }
        LOG.info("Got File Size: " + downloadedSize);
    }
}