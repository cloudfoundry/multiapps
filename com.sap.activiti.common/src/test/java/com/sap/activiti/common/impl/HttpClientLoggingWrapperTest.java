package com.sap.activiti.common.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HttpClientLoggingWrapperTest {
	
	private static int[] NON_ERROR_STATUS_CODES = new int[] {100, 101, 200, 201, 202, 203, 204, 205, 206};
	private HttpLogger logger;
	private HttpClient httpClient;
	private TestableHttpClientLoggingWrapper wrapper;
	
	@Before
	public void setUp() throws URISyntaxException {
		logger = Mockito.mock(HttpLogger.class);
		httpClient = Mockito.mock(HttpClient.class);
		wrapper = new TestableHttpClientLoggingWrapper(httpClient, new URI("http://sap.com"), true);
		wrapper.setLogger(logger);
	}
	
	@Test
	public void testNoErrorLogForNonErrorStatusCodes() throws Exception {
		for (int statusCode : NON_ERROR_STATUS_CODES) {
			verifyLogErrorCount(statusCode, 0);
		}
	}
	
	@Test
	public void testErrorLogForErrorStatusCodes() throws Exception {
		for (int statusCode = 300; statusCode < 600; statusCode++) {
			verifyLogErrorCount(statusCode, 1);
		}
	}
	
	private void verifyLogErrorCount(int statusCode, int logErrorCount) throws Exception{
		reset(logger);
		HttpResponse response = createResponse(statusCode);
		when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(response);
		wrapper.execute(mock(HttpUriRequest.class));
		verify(logger, times(logErrorCount)).error(any(HttpUriRequest.class), eq(response), any(URI.class), any(Boolean.class));
	}
	
	private HttpResponse createResponse(int statusCode) {
		StatusLine statusLine = mock(StatusLine.class);
		HttpResponse response = mock(HttpResponse.class);
		when(statusLine.getStatusCode()).thenReturn(statusCode);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(response.toString()).thenReturn("Status code: " + statusCode);
		return response;
	}
	
	private static class TestableHttpClientLoggingWrapper extends HttpClientLoggingWrapper {
		
		private HttpLogger logger;

		public TestableHttpClientLoggingWrapper(HttpClient httpClient,
				URI targetURL, boolean verboseMode) {
			super(httpClient, targetURL, verboseMode);
		}
		
		@Override
		protected HttpLogger getLogger() {
			return logger;
		}
		
		public void setLogger(HttpLogger logger) {
			this.logger = logger;
		}
	}
}
 