package org.cloudfoundry.multiapps.mta.handlers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator;
import org.junit.jupiter.api.Test;

class HandlerFactoryTest {

    @Test
    void testGetV2DescriptorHandler() {
        DescriptorHandler handler = HandlerFactory.forSchemaVersion(2)
                                                  .getDescriptorHandler();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler);
    }

    @Test
    void testGetV3DescriptorHandler() {
        DescriptorHandler handler = HandlerFactory.forSchemaVersion(3)
                                                  .getDescriptorHandler();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler);
    }

    @Test
    void testGetUnsupportedDescriptorHandler() {
        assertThrows(UnsupportedOperationException.class, () -> HandlerFactory.forSchemaVersion(128)
                                                                              .getDescriptorHandler());
    }

    @Test
    void testGetV2DescriptorValidator() {
        DescriptorValidator handler = HandlerFactory.forSchemaVersion(2)
                                                    .getDescriptorValidator();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator);
    }

    @Test
    void testGetV3DescriptorValidator() {
        DescriptorValidator handler = HandlerFactory.forSchemaVersion(3)
                                                    .getDescriptorValidator();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorValidator);
    }

    @Test
    void testGetUnsupportedDescriptorValidator() {
        assertThrows(UnsupportedOperationException.class, () -> HandlerFactory.forSchemaVersion(128)
                                                                              .getDescriptorValidator());
    }

    @Test
    void testGetV2DescriptorParser() {
        DescriptorParser handler = HandlerFactory.forSchemaVersion(2)
                                                 .getDescriptorParser();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser);
    }

    @Test
    void testGetV3DescriptorParser() {
        DescriptorParser handler = HandlerFactory.forSchemaVersion(3)
                                                 .getDescriptorParser();
        assertTrue(handler instanceof org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorParser);
    }

    @Test
    void testGetUnsupportedDescriptorParser() {
        assertThrows(UnsupportedOperationException.class, () -> HandlerFactory.forSchemaVersion(128)
                                                                              .getDescriptorParser());
    }

}
