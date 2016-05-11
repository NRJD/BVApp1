/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.task;

import org.nrjd.bv.app.metadata.CountryCallingCodeUtils;
import org.nrjd.bv.app.service.DataServiceProvider;
import org.nrjd.bv.app.service.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User registration task.
 */
public class RegisterTask extends BaseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTask.class);
    private String userId = null;
    private String password = null;
    private String name = null;
    private int mobileCountryCode = CountryCallingCodeUtils.getNoneCallingCode();
    private String mobileNumber = null;

    public RegisterTask(TaskContext taskContext, String userId, String password, String name, int mobileCountryCode, String mobileNumber) {
        super(taskContext);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public Response doInBackground(Void... params) {
        super.doBackgroundWork();
        Response response = null;
        try {
            response = getDataServiceProvider().performRegistration(this.userId, this.password, this.name, this.mobileCountryCode, this.mobileNumber);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while performing the user registration", e);
            response = constructErrorResponse(e);
        }
        return response;
    }
}