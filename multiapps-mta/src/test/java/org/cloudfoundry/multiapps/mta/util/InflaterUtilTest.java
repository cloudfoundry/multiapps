package org.cloudfoundry.multiapps.mta.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.BoundedInputStream;
import org.cloudfoundry.multiapps.common.ContentException;
import org.junit.jupiter.api.Test;

class InflaterUtilTest {

    private static final int DEPLOYMENT_DESCRIPTOR_ENTRY_START_POSITION = 271;
    private static final int DEPLOYMENT_DESCRIPTOR_ENTRY_END_POSITION = 315;

    @Test
    void extractDeploymentDescriptor() throws IOException {
        InputStream inputStream = getDeploymentDescriptorEntryStream();
        StringBuilder actualDeploymentDescriptor = new StringBuilder();
        InflatorUtil.inflate(new EntryToInflate("mtad.yaml", Integer.MAX_VALUE, inputStream), (readBytesBuffer, numberOfBytesRead) -> {
            actualDeploymentDescriptor.append(new String(readBytesBuffer, 0, numberOfBytesRead));
        });
        String expectedDeploymentDescriptor = new String(getClass().getResourceAsStream("expected-deployment-descriptor.yaml")
                                                                   .readAllBytes());
        assertEquals(expectedDeploymentDescriptor, actualDeploymentDescriptor.toString());
    }

    private BoundedInputStream getDeploymentDescriptorEntryStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("deflated-mta.mtar");
        inputStream.skip(DEPLOYMENT_DESCRIPTOR_ENTRY_START_POSITION);
        return new BoundedInputStream.Builder().setInputStream(inputStream)
                                               .setCount(DEPLOYMENT_DESCRIPTOR_ENTRY_START_POSITION)
                                               .setMaxCount(DEPLOYMENT_DESCRIPTOR_ENTRY_END_POSITION)
                                               .get();
    }

    @Test
    void testWithEntryExceedingMaxSize() throws IOException {
        InputStream inputStream = getDeploymentDescriptorEntryStream();
        Exception exception = assertThrows(ContentException.class, () -> {
            InflatorUtil.inflate(new EntryToInflate("mtad.yaml", 1, inputStream), (readBytesBuffer, numberOfBytesRead) -> {
            });
        });
        assertEquals("The size \"49\" of mta file \"mtad.yaml\" exceeds the configured max size limit \"1\"", exception.getMessage());
    }

}
