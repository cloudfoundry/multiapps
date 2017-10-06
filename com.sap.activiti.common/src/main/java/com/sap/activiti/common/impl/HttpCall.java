package com.sap.activiti.common.impl;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HttpCall {

    private static final String UTC = "UTC";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static SimpleDateFormat dateFormatter;
    private String method;
    private String requestPayload;
    private int statusCode;
    private String responsePayload;
    private URI requestResourcePath;
    private URI requestDestinationURI;
    private String message;

    static {
        dateFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(UTC));
    }

    public void setRequestMethod(String method) {
        this.method = method;

    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;

    }

    public void setRequestRelativePath(URI requestResourcePath) {
        this.requestResourcePath = requestResourcePath;

    }

    public void setTargetURL(URI requestDestinationURI) {
        this.requestDestinationURI = requestDestinationURI;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;

    }

    public void setResponseStatus(int statusCode) {
        this.statusCode = statusCode;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String result = String.format("[%s] - Method: %s, RequestURL: %s%s, Request payload: %s, response payload: %s, status code: %d",
                dateFormatter.format(new Date()), method, requestDestinationURI, requestResourcePath == null ? "" : requestResourcePath,
                                requestPayload, responsePayload, statusCode);
        if (isNotBlank(message)) {
            result += String.format("\nMessage: %s", message);
        }
        return result;
    }
}
