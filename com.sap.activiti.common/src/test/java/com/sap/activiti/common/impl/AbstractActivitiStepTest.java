package com.sap.activiti.common.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.activiti.common.ExecutionStatus;

public class AbstractActivitiStepTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private DelegateExecution mockContext;
    private AbstractActivitiStep testStep;

    @Before
    public void setUp() {
        testStep = createStep();
        mockContext = MockDelegateExecution.createSpyInstance();
    }

    @Test
    public void testSignalFailureOnException() throws Exception {
        try {
            exception.expect(Exception.class);
            exception.expectMessage("test exception");
            testStep.execute(mockContext);
        } finally {
            verify(mockContext).setVariable(anyString(), eq(ExecutionStatus.FAILED.name()));
        }
    }

    @Test
    public void testSkipRequest() throws Exception {
        testStep.createSkipHelper(mockContext)
            .createSkipRequest(testStep.getLogicalStepName());
        testStep.execute(mockContext);

        assertEquals(ExecutionStatus.SKIPPED.toString(), mockContext.getVariable(testStep.getStatusVariable()));
        assertFalse(testStep.createSkipHelper(mockContext)
            .hasSkipRequest(testStep.getLogicalStepName()));
    }

    @Test
    public void testSkipRequest_whenTaskIsWithBridge() throws Exception {
        testStep = createStepWithBridge();
        testStep.createSkipHelper(mockContext)
            .createSkipRequest(testStep.getLogicalStepName());
        testStep.execute(mockContext);

        assertEquals(ExecutionStatus.SKIPPED.toString(), mockContext.getVariable(testStep.getStatusVariable()));
        assertTrue(testStep.createSkipHelper(mockContext)
            .hasSkipRequest(testStep.getLogicalStepName()));
    }

    private AbstractActivitiStep createStep() {
        return new AbstractActivitiStep() {
            @Override
            protected ExecutionStatus executeStep(DelegateExecution context) throws Exception {
                throw new Exception("test exception");
            }
        };
    }

    private AbstractActivitiStepWithBridge createStepWithBridge() {
        return new AbstractActivitiStepWithBridge() {
            @Override
            protected ExecutionStatus pollStatus(DelegateExecution context) throws Exception {
                throw new Exception("test exception");
            }
        };
    }
}
