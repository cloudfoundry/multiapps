package com.sap.cloud.lm.sl.slp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.sap.cloud.lm.sl.slp.TestUtils;

public class StepMetadataTest {

    @Test
    public void testEqual() {
        StepMetadata stepMetadata1 = StepMetadata.builder().id("stepId").build();
        StepMetadata stepMetadata2 = StepMetadata.builder().id("stepId").build();
        assertEquals(stepMetadata1, stepMetadata2);
        assertEquals(stepMetadata1, stepMetadata1);

        assertEquals(stepMetadata1.hashCode(), stepMetadata2.hashCode());
    }

    @Test
    public void testNotEqual() {
        StepMetadata stepMetadata1 = StepMetadata.builder().id("stepId1").build();
        StepMetadata stepMetadata2 = StepMetadata.builder().id("stepId2").build();
        assertFalse(stepMetadata1.equals(stepMetadata2));
        assertFalse(stepMetadata1.equals(null));
        assertFalse(stepMetadata1.equals(TestUtils.mockStepMetadata("stepId", true)));

        assertFalse(stepMetadata1.hashCode() == stepMetadata2.hashCode());
    }

}
