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
    private int minorVersion;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Version 1.0:
            {
                1, 0, null,
            },
            // (1) Version 2.0:
            {
                2, 0, null,
            },
            // (2) Version 3.1:
            {
                3, 1, null,
            },
            // (3) Unsupported version 0:
            {
                0, 0, "Version \"0\" is not supported",
            },
// @formatter:on
        });
    }

    public HandlerFactoryTest(int majorVersion, int minorVersion, String expectedExceptionMessage) {
        if (expectedExceptionMessage != null) {
            expectedException.expectMessage(expectedExceptionMessage);
        }
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    @Test
    public void testGetDescriptorHandler() {
        DescriptorHandler handler = new HandlerFactory(majorVersion, minorVersion).getDescriptorHandler();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorHandler);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler);
                break;
            case 3:
                if(minorVersion == 1) {
                    assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorHandler);
                }
                break;
        }
    }

    @Test
    public void testGetDescriptorValidator() {
        DescriptorValidator handler = new HandlerFactory(majorVersion, minorVersion).getDescriptorValidator();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorValidator);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorValidator);
                break;
            case 3:
                if(minorVersion == 1) {
                    assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorValidator);
                }
                break;
        }
    }

    @Test
    public void testGetDescriptorParser() {
        DescriptorParser handler = new HandlerFactory(majorVersion, minorVersion).getDescriptorParser();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof DescriptorParser);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser);
                break;
            case 3:
                if(minorVersion == 1) {
                    assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorParser);
                }
                break;
        }
    }

    @Test
    public void testGetConfigurationParser() {
        ConfigurationParser handler = new HandlerFactory(majorVersion, minorVersion).getConfigurationParser();
        switch (majorVersion) {
            case 1:
                assertTrue(handler instanceof ConfigurationParser);
                break;
            case 2:
                assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v2_0.ConfigurationParser);
                break;
            case 3:
                if(minorVersion == 1) {
                    assertTrue(handler instanceof com.sap.cloud.lm.sl.mta.handlers.v3_1.ConfigurationParser);
                }
                break;
        }
    }

}
