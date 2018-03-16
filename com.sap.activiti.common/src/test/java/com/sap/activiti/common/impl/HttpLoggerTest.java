package com.sap.activiti.common.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class HttpLoggerTest {
    private static final boolean VERBOSE_MODE_ENABLED = true;
    private static final String TEST_HTTP_METHOD = "POST";
    private static final String TEST_REQUEST_ENTITY_CONTENT = "{\"content\":\"Request entity content\"}";
    private static final String TEST_RESPONSE_ENTITY_CONTENT = "{\"content\":\"Response entity content\"}";
    private static final String TEST_LOGICAL_STEP_NAME = "TestStep";
    private static final String TEST_URI_PATH = "test/uri";
    private static final String TEST_URL = "http://localhost:8080/";
    private static final int TEST_RESPONSE_CODE = HttpStatus.SC_ACCEPTED;

    private String persistedLog;

    @Before
    public void setUp() throws URISyntaxException, ParseException, IOException {
        HttpLoggerTestPO pageObject = new HttpLoggerTestPO();

        HttpLogger.LOGGER = mock(Logger.class);

        HttpLogger httpLogger = HttpLogger.getInstance();

        httpLogger.enableLogCollection();

        DelegateExecution testContext = new MockDelegateExecution();

        HttpUriRequest mockRequest = pageObject.createMockRequest(TEST_HTTP_METHOD, TEST_REQUEST_ENTITY_CONTENT, TEST_URI_PATH);
        HttpResponse mockResponse = pageObject.createMockResponse(TEST_RESPONSE_ENTITY_CONTENT, TEST_RESPONSE_CODE);

        httpLogger.log(mockRequest, mockResponse, new URI(TEST_URL), VERBOSE_MODE_ENABLED);

        httpLogger.persistLog(TEST_LOGICAL_STEP_NAME, testContext);

        persistedLog = pageObject.getPersistedLog(testContext);

    }

    @After
    public void after() {
        HttpLogger.getInstance()
            .disableLogCollection();
    }

    @Test
    public void testPersistedLogContainsRequestEntity() throws Exception {
        assertTrue(persistedLog.contains(TEST_REQUEST_ENTITY_CONTENT));
    }

    @Test
    public void testPersistedLogContainsResponseEntity() throws Exception {
        assertTrue(persistedLog.contains(TEST_RESPONSE_ENTITY_CONTENT));
    }

    @Test
    public void testPersistedLogContainsRequestFullPath() throws Exception {
        assertTrue(persistedLog.contains(TEST_URL + TEST_URI_PATH));
    }

    @Test
    public void testPersistedLogContainsRequestMethod() throws Exception {
        assertTrue(persistedLog.contains(TEST_HTTP_METHOD));
    }

    @Test
    public void testPersistedLogContainsResponseCode() throws Exception {
        assertTrue(persistedLog.contains(TEST_RESPONSE_CODE + ""));
    }

    @Test
    public void testLogEntryWrittenInTraceFile() throws Exception {
        verify(HttpLogger.LOGGER).debug(anyString());
    }

}
