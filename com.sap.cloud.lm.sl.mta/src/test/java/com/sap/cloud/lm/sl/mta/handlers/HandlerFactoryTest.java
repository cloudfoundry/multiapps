package com.sap.cloud.lm.sl.mta.handlers;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorValidator;

@RunWith(value = Parameterized.class)
public class HandlerFactoryTest {

    private int majorVersion;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Version 1.0:
            {
                1, null,
            },
            // (1) Version 2.0:
            {
                2, null,
            },
            // (2) Unsupported version 0:
            {
                0, "Version \"0\" is not supported",
            },
// @formatter:on
        });
    }

    public HandlerFactoryTest(int majorVersion, String expectedExceptionMessage) {
        if (expectedExceptionMessage != null) {
            expectedException.expectMessage(expectedExceptionMessage);
        }
        this.majorVersion = majorVersion;
    }

    @Test
    public void testGetDescriptorHandler() {
        DescriptorHandler handler = new HandlerFactory(majorVersion).getDescriptorHandler();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorHandler);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler);
                break;
        }
    }

    @Test
    public void testGetDescriptorValidator() {
        DescriptorValidator handler = new HandlerFactory(majorVersion).getDescriptorValidator();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorValidator);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorValidator);
                break;
        }
    }

    @Test
    public void testGetDescriptorParser() {
        DescriptorParser handler = new HandlerFactory(majorVersion).getDescriptorParser();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorParser);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser);
                break;
        }
    }

    @Test
    public void testGetConfigurationParser() {
        ConfigurationParser handler = new HandlerFactory(majorVersion).getConfigurationParser();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof ConfigurationParser);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParser);
                break;
        }
    }

}
