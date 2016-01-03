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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.nrjd.bv.app.ctx.AppContext;
import org.nrjd.bv.app.net.NetworkServiceUtils;
import org.nrjd.bv.app.util.AppConstants;
import org.nrjd.bv.app.util.CommonUtils;
import org.nrjd.bv.app.util.ErrorCode;
import org.nrjd.bv.app.util.PatternUtils;
import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.nrjd.bv.app.service.DataServiceParameters.CMD_LOGIN;
import static org.nrjd.bv.app.service.DataServiceParameters.CMD_REGISTER;
import static org.nrjd.bv.app.service.DataServiceParameters.CMD_VERIFY_ACCOUNT;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_CMD;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_COUNTRY_CODE;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_EMAIL;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_EMAIL_VERIFICATION_CODE;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_NAME;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_PASSWORD;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_PHONE_NUMBER;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_RESET_PASSWORD_ENABLED;
import static org.nrjd.bv.app.service.DataServiceParameters.PARAM_STATUS_ID;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_ACCT_ALREADY_VERIFIED;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_ACCT_NOT_VERIFIED;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_ACCT_VERIFIED;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_DUPLICATE_EMAIL_ID;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_LOGIN_FAILED_INVALID_CREDENTIALS;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_LOGIN_SUCCESS;
import static org.nrjd.bv.app.service.DataServiceParameters.STATUS_USER_ADD;

/**
 * Data service provider.
 */
public class DataServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataServiceProvider.class);
    private static final HttpClient HTTP_CLIENT = PageTurnerModule.getSSLHttpClient();
    // Server URL constants
    // TODO: Move them to common location.
    private static final String BASE_SERVER_URL = "http://52.32.43.240:8011/BVServer";
    // private static final String BASE_SERVER_URL = "http://10.0.0.5:8011/BVServer";
    private static final String SERVER_DATA_URL = BASE_SERVER_URL + "/mobileReq";

    // Data
    private AppContext appContext = null;

    public DataServiceProvider(AppContext appContext) {
        this.appContext = appContext;
    }

    private static String getServerDataUrl() {
        return SERVER_DATA_URL;
    }

    public Response performLogin(String userId, String password) throws DataServiceException {
        // Validate parameters.
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_EMAIL_ADDRESS);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_PASSWORD);
        }
        // Construct json data.
        JSONObject jsonRequestData = new JSONObject();
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_CMD, CMD_LOGIN);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_EMAIL, userId);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_PASSWORD, password);
        JSONObject jsonResponseData = processServerRequest(jsonRequestData);
        // Process response data
        if (jsonRequestData != null) {
            // TODO: Show the error message for ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_REGISTERED
            String statusId = JsonUtils.getJsonParameter(jsonResponseData, PARAM_STATUS_ID);
            if (STATUS_LOGIN_SUCCESS.equalsIgnoreCase(statusId)) {
                Response response = Response.createSuccessResponse();
                String resetPasswordEnabled = JsonUtils.getJsonParameter(jsonResponseData, PARAM_RESET_PASSWORD_ENABLED);
                ResponseDataUtils.setIsTempPassword(response, Boolean.valueOf(resetPasswordEnabled));
                return response;
            } else if (STATUS_ACCT_NOT_VERIFIED.equalsIgnoreCase(statusId)) {
                return Response.createFailedResponse(ErrorCode.EC_LOGIN__EMAIL_ADDRESS_NOT_VERIFIED);
            } else if (STATUS_LOGIN_FAILED_INVALID_CREDENTIALS.equalsIgnoreCase(statusId)) {
                return Response.createFailedResponse(ErrorCode.EC_LOGIN__INVALID_PASSWORD);
            }
        }
        return Response.getServiceErrorResponse();
    }

    public Response performRegistration(String userId, String password, String name, String mobileCountryCode, String mobileNumber) throws DataServiceException {
        // Validate parameters.
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMPTY_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_REGISTER__INVALID_EMAIL_ADDRESS);
        }
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
        // Construct json data.
        JSONObject jsonRequestData = new JSONObject();
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_CMD, CMD_REGISTER);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_EMAIL, userId);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_PASSWORD, password);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_NAME, name);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_COUNTRY_CODE, mobileCountryCode);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_PHONE_NUMBER, mobileNumber);
        JSONObject jsonResponseData = processServerRequest(jsonRequestData);
        // Process response data
        if (jsonRequestData != null) {
            String statusId = JsonUtils.getJsonParameter(jsonResponseData, PARAM_STATUS_ID);
            if (STATUS_USER_ADD.equalsIgnoreCase(statusId)) {
                return Response.createSuccessResponse();
            } else if (STATUS_DUPLICATE_EMAIL_ID.equalsIgnoreCase(statusId) || STATUS_ACCT_NOT_VERIFIED.equalsIgnoreCase(statusId)) {
                return Response.createFailedResponse(ErrorCode.EC_REGISTER__EMAIL_ADDRESS_ALREADY_REGISTERED);
            }
        }
        return Response.getServiceErrorResponse();
    }

    public Response verifyUserId(String userId, String userIdVerificationCode) throws DataServiceException {
        // Validate parameters.
        if (StringUtils.isNullOrEmpty(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS);
        }
        if (!PatternUtils.isValidEmailAddress(userId)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS);
        }
        if (StringUtils.isNullOrEmpty(userIdVerificationCode)) {
            return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE);
        }
        // Construct json data.
        JSONObject jsonRequestData = new JSONObject();
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_CMD, CMD_VERIFY_ACCOUNT);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_EMAIL, userId);
        JsonUtils.addJsonParameter(jsonRequestData, PARAM_EMAIL_VERIFICATION_CODE, userIdVerificationCode);
        JSONObject jsonResponseData = processServerRequest(jsonRequestData);
        // Process response data
        if (jsonRequestData != null) {
            // TODO: Distinctly show error messages for ErrorCode.EC_VERIFY_ACCOUNT__EMAIL_ADDRESS_NOT_REGISTERED
            // and ErrorCode.EC_VERIFY_ACCOUNT__INVALID_EMAIL_ADDRESS_VERIFICATION_CODE
            String statusId = JsonUtils.getJsonParameter(jsonResponseData, PARAM_STATUS_ID);
            if (STATUS_ACCT_VERIFIED.equalsIgnoreCase(statusId)) {
                return Response.createSuccessResponse();
            } else if (STATUS_ACCT_ALREADY_VERIFIED.equalsIgnoreCase(statusId)) {
                return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__EMAIL_ADDRESS_ALREADY_VERIFIED);
            } else if (STATUS_ACCT_NOT_VERIFIED.equalsIgnoreCase(statusId)) {
                return Response.createFailedResponse(ErrorCode.EC_VERIFY_ACCOUNT__EMAIL_ADDRESS_NOT_VERIFIED);
            }
        }
        return Response.getServiceErrorResponse();
    }

    private JSONObject processServerRequest(JSONObject jsonObject) throws DataServiceException {
        JSONObject jsonResponseData = null;
        try {
            HttpPost request = new HttpPost(getServerDataUrl());
            String postData = JsonUtils.generateJsonData(jsonObject);
            if (StringUtils.isNotNullOrEmpty(postData)) {
                StringEntity stringEntity = new StringEntity(postData);
                request.setEntity(stringEntity);
            }
            HttpResponse response = HTTP_CLIENT.execute(request);
            int httpResponseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 200) {
                jsonResponseData = readResponse(response);
            } else {
                throw new DataServiceException("Service error: Http Status Code: " + httpResponseCode,
                        null, ErrorCode.EC_SERVICE_ERROR_HTTP_STATUS_CODE_ERROR);
            }
        } catch (DataServiceException e) {
            throw e;
        } catch (Exception e) {
            if (DataServiceUtils.containsTimeoutExceptionDuringRead(e)) {
                throw new DataServiceException(e, ErrorCode.EC_SERVICE_ERROR_NETWORK_TIMEOUT_DURING_READ);
            } else if (DataServiceUtils.containsConnectionException(e)) {
                if (!NetworkServiceUtils.isNetworkOn(this.appContext)) {
                    throw new DataServiceException(e, ErrorCode.EC_SERVICE_ERROR_NO_NETWORK_CONNECTION);
                } else {
                    throw new DataServiceException(e, ErrorCode.EC_SERVICE_ERROR_COULD_NOT_CONNECT_TO_SERVICE);
                }
            }
        }
        return jsonResponseData;
    }

    private JSONObject readResponse(HttpResponse response) throws Exception {
        HttpEntity httpEntity = response.getEntity();
        long dataSize = httpEntity.getContentLength();
        LOGGER.debug("Got content-length: {}", dataSize);
        InputStream inputStream = httpEntity.getContent();
        String data = readData(inputStream);
        JSONObject jsonResponseData = JsonUtils.parseJsonData(data);
        return jsonResponseData;
    }

    private String readData(InputStream inputStream) throws IOException {
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
        LOGGER.debug("Got Data: {} characters:\n{}", downloadedSize, data);
        return data;
    }
}