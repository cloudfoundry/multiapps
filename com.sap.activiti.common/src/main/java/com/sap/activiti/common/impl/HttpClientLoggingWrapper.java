package com.sap.activiti.common.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class HttpClientLoggingWrapper implements HttpClient {

    private HttpClient httpClient;
    private URI targetURL;
    private boolean verboseMode;

    public HttpClientLoggingWrapper(HttpClient httpClient, URI targetURL, boolean verboseMode) {
        this.httpClient = httpClient;
        this.targetURL = targetURL;
        this.verboseMode = verboseMode;
    }

    @Override
    public HttpParams getParams() {
        return httpClient.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return httpClient.getConnectionManager();
    }

    @Override
    public HttpResponse execute(final HttpUriRequest request) throws IOException, ClientProtocolException {

        return logHttpCommunication(request, new HttpCallback() {

            @Override
            public HttpResponse execute() throws ClientProtocolException, IOException {
                return httpClient.execute(request);
            }
        });
    }

    @Override
    public HttpResponse execute(final HttpUriRequest request, final HttpContext context) throws IOException, ClientProtocolException {

        return logHttpCommunication(request, new HttpCallback() {

            @Override
            public HttpResponse execute() throws ClientProtocolException, IOException {
                return httpClient.execute(request, context);
            }

        });
    }

    private HttpResponse logHttpCommunication(HttpUriRequest request, HttpCallback httpCall) throws IOException {
        HttpResponse response = null;

        try {
            response = httpCall.execute();
            log(request, response);

            return response;
        } catch (IOException io) {
            error(request, response);
            throw io;
        } catch (Exception e) {
            error(request, response);
            throw new RuntimeException(e);
        }
    }

    private interface HttpCallback {
        public HttpResponse execute() throws ClientProtocolException, IOException;
    }

    private void error(HttpUriRequest request, HttpResponse response) throws IOException {
        getLogger().error(request, response, targetURL, verboseMode);
    }

    private void log(HttpUriRequest request, HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine()
            .getStatusCode();

        if (isSuccessfulCode(statusCode)) {
            getLogger().log(request, response, targetURL, verboseMode);
        } else {
            getLogger().error(request, response, targetURL, verboseMode);
        }
    }

    private static boolean isSuccessfulCode(int statusCode) {
        return statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return httpClient.execute(target, request);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return httpClient.execute(target, request, context);
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return httpClient.execute(request, responseHandler);
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
        throws IOException, ClientProtocolException {
        return httpClient.execute(request, responseHandler, context);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
        throws IOException, ClientProtocolException {
        return httpClient.execute(target, request, responseHandler);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
        throws IOException, ClientProtocolException {
        return httpClient.execute(target, request, responseHandler, context);
    }

    protected HttpLogger getLogger() {
        return HttpLogger.getInstance();
    }
}
