package com.sap.activiti.common.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpLoggerPasswordsMaskingTest {
    private static final String TEST_HTTP_METHOD = "POST";
    private static final String JSON_REQUEST_ENTITY_PASSWORD_CONTENT = "{\"system_pwd\":\"abcd1234\"}";
    private static final String JSON_REQUEST_ENTITY_WITH_MASKED_PASSWORD = "{\"system_pwd\":\"***\"}";

    private static final String NON_JSON_REQUEST_ENTITY_PASSWORD_CONTENT = "{<system_pwd>abcd1234</system_pwd>";
    private static final String TEST_LOGICAL_STEP_NAME = "TestStep";
    private static final String TEST_URI_PATH = "test/uri";
    private static final String TEST_URL = "http://localhost:8080/";

    private HttpLoggerTestPO pageObject;
    private HttpLogger logger;
    private DelegateExecution testContext;

    @Before
    public void setUp() throws URISyntaxException, ParseException, IOException {
        logger = HttpLogger.getInstance();
        logger.enableLogCollection();

        testContext = new MockDelegateExecution();
        pageObject = new HttpLoggerTestPO();
    }

    @After
    public void after() {
        HttpLogger.getInstance().disableLogCollection();
    }

    @Test
    public void testJsonPasswordsPropertyValuesAreMasked() throws Exception {
        addToContextLog(TEST_HTTP_METHOD, JSON_REQUEST_ENTITY_PASSWORD_CONTENT, TEST_URI_PATH);

        String persistedLog = pageObject.getPersistedLog(testContext);

        assertTrue(persistedLog.contains(JSON_REQUEST_ENTITY_WITH_MASKED_PASSWORD));
    }

    @Test
    public void testNonJsonEntitiesNotLogged() throws Exception {
        addToContextLog(TEST_HTTP_METHOD, NON_JSON_REQUEST_ENTITY_PASSWORD_CONTENT, TEST_URI_PATH);

        String persistedLog = pageObject.getPersistedLog(testContext);

        assertTrue(persistedLog.contains(HttpLogger.NON_JSON_CONTENT_ENTITY_ERROR_MESSAGE));
    }

    private void addToContextLog(String httpMethod, String jsonRequestEntityPasswordContent, String uriPath)
        throws URISyntaxException, UnsupportedEncodingException, IOException {

        HttpUriRequest mockRequest = pageObject.createMockRequest(httpMethod, jsonRequestEntityPasswordContent, uriPath);

        logger.log(mockRequest, null, new URI(TEST_URL), true);

        logger.persistLog(TEST_LOGICAL_STEP_NAME, testContext);
    }

}
