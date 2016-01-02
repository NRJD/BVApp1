/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.service;


import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.ssl.EasySSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.nrjd.bv.app.util.CommonUtils;
import org.nrjd.bv.app.util.ErrorCode;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * User login task.
 */
public class ServiceDataProvider2 {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceDataProvider2.class);

//    @Inject
//    private HttpClient http;

    /**
     * Private constructor to prevents the class from being instantiated.
     */
    private ServiceDataProvider2() {
//        RoboGuice.injectMembers(context, this);
    }

    /**
     * Returns a singleton ServiceDataProvider2 instance.
     *
     * @return a singleton ServiceDataProvider2 instance.
     */
    public static ServiceDataProvider2 getInstance() {
        return new ServiceDataProvider2();
    }

    public Response performRegistration(String userId, String password, String name, String mobileCountryCode, String mobileNumber) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__INVALID_EMAIL_ADDRESS);
        }
        // Verify user id
//        if (VALID_USER_IDS.containsKey(userId)) {
//            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMAIL_ADDRESS_ALREADY_REGISTERED);
//        }
        if (StringUtils.isNullOrEmpty(password)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(name)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_NAME);
        }
        if (StringUtils.isNullOrEmpty(mobileCountryCode)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_MOBILE_COUNTRY_CODE);
        }
        if (StringUtils.isNullOrEmpty(mobileNumber)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_MOBILE_NUMBER);
        }
        postData(null, userId, password, name, mobileCountryCode, mobileNumber);
        return Response.createSuccessResponse();
    }

    private void postData(Configuration config, String userId, String password, String name, String mobileCountryCode, String mobileNumber) {
        String bookUrl = "http://127.0.0.1:8011/BVServer/mobileReq";
        // Validations.
        // If bookUrl specified is null or empty, then return from here.
        if (StringUtils.isNullOrEmpty(bookUrl)) {
            return;
        }
        // Download the book.
        try {
            LOG.debug("Posting: " + bookUrl);

            HttpPost post = new HttpPost(bookUrl);
            if(config != null) {
                post.setHeader("User-Agent", config.getUserAgent());
            }
            // post data
            String postData =
                    "{\"cmd\":\"register\"" +
                            ",\"name\":\"" + name + "\"" +
                            ",\"password\":\"" + password + "\"" +
                            ",\"email\":\"" + userId + "\"" +
                            ",\"phoneNum\":\"" + mobileCountryCode + mobileNumber + "\"" +
                            ",\"language\":\"Eng\"" +
                            "}";
            StringEntity stringEntity = new StringEntity(postData);
            post.setEntity(stringEntity);
            SSLHttpClient http = new SSLHttpClient(new BasicHttpParams());
            HttpResponse response = http.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                downloadBook(response);
            } else {
                // TODO: Don't show internal exception messages to end users.
                LOG.error(bookUrl + ": Error" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            // TODO: Don't show internal exception messages to end users.
            LOG.error(bookUrl + ": Error occurred", e);
        }
    }

    private void downloadBook(HttpResponse response) throws Exception {
        // File size is used for calculating the download progress.
        // If we don't get proper content-length in http response, then assign it from book entry registry.
        long fileSize = response.getEntity().getContentLength();
        if ((fileSize < 0)) {
            LOG.debug("Got invalid content-length: " + fileSize);
            fileSize = 0;
        }
        LOG.debug("Got content-length: " + fileSize);
        Header type = response.getEntity().getContentType();
        long downloadedSize = 0;
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            in = response.getEntity().getContent();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4098];
            int readSize = 0;
            while ((readSize = in.read(buffer)) > 0) { // && !isCancelled()) {
                // Make sure the user can cancel the download.
                // if (isCancelled()) {
                   // return;
                // }
                baos.write(buffer, 0, readSize);
                downloadedSize += readSize;
            }
            String downloadedString = baos.toString();
            LOG.debug("Downloaded: " + readSize + "bytes: " + downloadedString);
        } finally {
            CommonUtils.closeQuietly(baos);
            CommonUtils.closeQuietly(in);
        }
    }

    public static class SSLHttpClient extends DefaultHttpClient {


        public SSLHttpClient(HttpParams params) {
            super(params);
        }

        @Override protected ClientConnectionManager createClientConnectionManager() {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(
                    new Scheme("http", PlainSocketFactory.getSocketFactory(), 8011));
            registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
            return new SingleClientConnManager(getParams(), registry);
        }

    }
}