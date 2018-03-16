package com.sap.activiti.common.impl;

import static com.sap.activiti.common.Constants.CHARSET_UTF_8;
import static com.sap.activiti.common.Constants.HTTP_LOG_SUFFIX;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.activiti.common.util.PasswordMasker;

public class HttpLogger {

    public static final String NON_JSON_CONTENT_ENTITY_ERROR_MESSAGE = "Tring to add non-json payload to the log. Only json payloads are supported.";
    static Logger LOGGER = LoggerFactory.getLogger(HttpLogger.class);
    private static ThreadLocal<HttpLogger> threadDedicatedLoggers = new ThreadLocal<HttpLogger>();
    private List<HttpCall> loggedMessages;
    private boolean isLogInMemoryEnabled;

    public static HttpLogger getInstance() {
        HttpLogger threadDedicatedLogger = threadDedicatedLoggers.get();
        return threadDedicatedLogger != null ? threadDedicatedLogger : new HttpLogger();
    }

    private HttpLogger() {
        isLogInMemoryEnabled = false;
        loggedMessages = new LinkedList<HttpCall>();
    }

    public void error(HttpUriRequest request, HttpResponse response, URI targetURL, boolean verboseMode)
        throws ParseException, IOException {

        if (isLogInMemoryEnabled || verboseMode) {
            String logEntry = addToInMemoryLog(request, response, targetURL);

            if (verboseMode) {
                LOGGER.error(logEntry);
            }
        }
    }

    public void log(HttpUriRequest request, HttpResponse response, URI targetURL, boolean verboseMode) throws ParseException, IOException {

        if (isLogInMemoryEnabled || verboseMode) {
            String logEntry = addToInMemoryLog(request, response, targetURL);

            if (verboseMode) {
                LOGGER.debug(logEntry);
            }
        }
    }

    private String addToInMemoryLog(HttpUriRequest request, HttpResponse response, URI targetURL) throws IOException {
        HttpCall call = createLog(request, response, targetURL);

        if (isLogCollectionEnabled()) {
            loggedMessages.add(call);
        }
        return call.toString();
    }

    private HttpCall createLog(HttpUriRequest request, HttpResponse response, URI targetURL) throws IOException {
        HttpCall call = new HttpCall();
        call.setTargetURL(targetURL);
        call.setRequestRelativePath(request.getURI());
        String requestMethod = request.getMethod();
        call.setRequestMethod(requestMethod);
        Header errorHeader = null;
        if (response != null && (errorHeader = response.getFirstHeader("errorMessage")) != null) {
            call.setMessage(errorHeader.getValue());
        }

        if (requestMethod.equals("PUT") || requestMethod.equals("POST")) {
            HttpEntityEnclosingRequestBase putRequest = (HttpEntityEnclosingRequestBase) request;
            HttpEntity repeatebleEntity = createRepeatebleEntity(putRequest.getEntity());
            putRequest.setEntity(repeatebleEntity);
            call.setRequestPayload(maskPasswords(extractPayloadContent(repeatebleEntity)));
        }

        if (response != null) {
            HttpEntity repetableEntity = createRepeatebleEntity(response.getEntity());
            response.setEntity(repetableEntity);
            call.setResponsePayload(extractPayloadContent(repetableEntity));
            call.setResponseStatus(response.getStatusLine()
                .getStatusCode());
        }
        return call;
    }

    private HttpEntity createRepeatebleEntity(HttpEntity entity) throws IOException {
        return entity == null ? entity : new BufferedHttpEntity(entity);
    }

    private String extractPayloadContent(HttpEntity entity) throws ParseException, IOException {
        String payload = null;

        if (entity != null) {
            payload = EntityUtils.toString(entity);
        }

        return payload;
    }

    public void persistLog(String logicalStepName, DelegateExecution context) throws UnsupportedEncodingException {

        String httpLogVariable = logicalStepName + HTTP_LOG_SUFFIX;

        byte[] existingHttpLogBinary = (byte[]) context.getVariable(httpLogVariable);
        String existingHttpLogString = existingHttpLogBinary == null ? "" : new String(existingHttpLogBinary, CHARSET_UTF_8);

        String httpLog = concatenate(existingHttpLogString, loggedMessages);

        if (StringUtils.isNotEmpty(httpLog)) {
            context.setVariable(httpLogVariable, httpLog.getBytes(CHARSET_UTF_8));
        }
    }

    public List<HttpCall> getLoggedMessages() {
        return loggedMessages;
    }

    public synchronized boolean isLogCollectionEnabled() {
        return isLogInMemoryEnabled;
    }

    public synchronized void disableLogCollection() {
        this.isLogInMemoryEnabled = false;
        this.loggedMessages.clear();
        threadDedicatedLoggers.remove();
    }

    public synchronized void enableLogCollection() {
        this.isLogInMemoryEnabled = true;
        threadDedicatedLoggers.set(this);
    }

    private String concatenate(String oldLogMessages, List<HttpCall> newLogMessages) {
        StringBuilder allMessages = new StringBuilder(oldLogMessages);
        for (HttpCall httpCall : newLogMessages) {
            allMessages.append(httpCall.toString() + "\n");
        }
        return allMessages.toString();
    }

    private String maskPasswords(String jsonPayload) {
        String jsonPayloadWithMaskedPwds = jsonPayload;
        try {
            jsonPayloadWithMaskedPwds = new PasswordMasker().maskPasswordsFromJson(jsonPayload);
        } catch (Exception e) {
            jsonPayloadWithMaskedPwds = NON_JSON_CONTENT_ENTITY_ERROR_MESSAGE;
            LOGGER.debug("Failed to mask passwords in payload.", e);
        }
        return jsonPayloadWithMaskedPwds;
    }
}
