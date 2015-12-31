/*
 * Copyright (C) 2016 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app;

import net.nightwhistler.pageturner.PageTurner;

import org.acra.annotation.ReportsCrashes;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.BRAND;
import static org.acra.ReportField.BUILD;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PACKAGE_NAME;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.PRODUCT;
import static org.acra.ReportField.REPORT_ID;
import static org.acra.ReportField.STACK_TRACE;


@ReportsCrashes(formKey = "", // will not be used
        formUri = "http://acra.pageturner-reader.org/crash",
        customReportContent = {REPORT_ID, APP_VERSION_CODE, APP_VERSION_NAME, ANDROID_VERSION, BRAND, PHONE_MODEL, BUILD, PRODUCT, STACK_TRACE, LOGCAT, PACKAGE_NAME}
)
public class BhaktiVriksha extends PageTurner {
}
