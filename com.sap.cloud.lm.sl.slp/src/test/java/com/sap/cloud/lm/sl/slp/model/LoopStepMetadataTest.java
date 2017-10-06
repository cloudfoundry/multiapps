package com.sap.cloud.lm.sl.slp.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LoopStepMetadataTest {

    private static final Object VALUE_LOOP_COUNT = 2;
    private static final Object VALUE_LOOP_COUNT_2 = 4;
    private static final String VAR_LOOP_COUNT = "LOOP_COUNT";

    @Test
    public void testGetSteps() {
        VariableHandler variableHandler = mock(VariableHandler.class);
        stub(variableHandler.getVariablesIncludingFromSubProcesses(VAR_LOOP_COUNT)).toReturn(Arrays.asList(VALUE_LOOP_COUNT));

        StepMetadata child1 = StepMetadata.builder().id("child1").build();
        StepMetadata child2 = StepMetadata.builder().id("child2").build();
        List<StepMetadata> children = Arrays.asList(child1, child2);
        LoopStepMetadata loopStep = LoopStepMetadata.builder().id("loop").children(children).countVariable(VAR_LOOP_COUNT).build();

        validateMetadata(variableHandler, children, loopStep, (int) VALUE_LOOP_COUNT);
    }

    @Test
    public void testGetStepsWithMergeCountVariable() {
        VariableHandler variableHandler = mock(VariableHandler.class);
        stub(variableHandler.getVariablesIncludingFromSubProcesses(VAR_LOOP_COUNT)).toReturn(
            Arrays.asList(VALUE_LOOP_COUNT, VALUE_LOOP_COUNT_2));

        StepMetadata child1 = StepMetadata.builder().id("child1").build();
        StepMetadata child2 = StepMetadata.builder().id("child2").build();
        List<StepMetadata> children = Arrays.asList(child1, child2);
        LoopStepMetadata loopStep = LoopStepMetadata.builder().id("loop").children(children).countVariable(VAR_LOOP_COUNT).build();

        validateMetadata(variableHandler, children, loopStep, (int) VALUE_LOOP_COUNT + (int) VALUE_LOOP_COUNT_2);
    }

    private void validateMetadata(VariableHandler variableHandler, List<StepMetadata> children, LoopStepMetadata loopStep,
        Integer expectedLoopCount) {
        assertEquals(expectedLoopCount * children.size(), loopStep.getChildren(variableHandler).size());
    }

}
