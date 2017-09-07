package com.sap.activiti.common.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicStatusLine;

import com.sap.activiti.common.Constants;

public class HttpLoggerTestPO {

    public HttpResponse createMockResponse(String responseEntityContent, int responseCode) throws UnsupportedEncodingException {
        HttpResponse mockedResponse = mock(HttpResponse.class);

        HttpEntity testEntity = new StringEntity(responseEntityContent, Constants.CHARSET_UTF_8);
        when(mockedResponse.getEntity()).thenReturn(testEntity);

        StatusLine testStatusLine = new BasicStatusLine(new ProtocolVersion("", 0, 0), responseCode, null);
        when(mockedResponse.getStatusLine()).thenReturn(testStatusLine);
        mockedResponse.getStatusLine().getStatusCode();

        return mockedResponse;
    }

    public HttpUriRequest createMockRequest(final String httpMethod, String requestEntityContent, String uri)
        throws URISyntaxException, UnsupportedEncodingException {
        HttpEntityEnclosingRequestBase mockedRequest = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return httpMethod;
            }
        };
        mockedRequest.setEntity(new StringEntity(requestEntityContent, Constants.CHARSET_UTF_8));
        mockedRequest.setURI(new URI(uri));

        return mockedRequest;
    }

    public String getPersistedLog(DelegateExecution testContext) throws UnsupportedEncodingException {
        Set<Entry<String, Object>> allEntries = testContext.getVariables().entrySet();
        Entry<String, Object> firstEntry = allEntries.iterator().next();

        return new String((byte[]) firstEntry.getValue(), Constants.CHARSET_UTF_8);
    }

}
