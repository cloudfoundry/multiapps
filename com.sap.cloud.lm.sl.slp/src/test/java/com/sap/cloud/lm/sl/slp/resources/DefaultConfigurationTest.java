package com.sap.cloud.lm.sl.slp.resources;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;
import com.sap.cloud.lm.sl.persistence.services.FileContentProcessor;

public class DefaultConfigurationTest {

    private DefaultConfiguration classUnderTest = null;

    @Test
    public void testNoAdditionalProcessingIsDone() {
        classUnderTest = new DefaultConfiguration();
        assertTrue(classUnderTest.getFileDownloadProcessor(mock(FileEntry.class),
            mock(FileContentProcessor.class)) instanceof DefaultFileDownloadProcessor);
        assertTrue(classUnderTest.getFileUploadProcessor(".mtaext") instanceof DefaultFileUploadProcessor);
    }

}
