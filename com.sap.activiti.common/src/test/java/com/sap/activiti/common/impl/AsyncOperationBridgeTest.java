package com.sap.activiti.common.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class AsyncOperationBridgeTest {

    private static final String LOGICAL_OPERATION_STEP_NAME = "stepName";
    private static final String OPERATION_ID = "operation";

    private DelegateExecution contextSpy;
    private AsyncOperationBridge bridge;

    @Before
    public void setUp() {
        contextSpy = MockDelegateExecution.createSpyInstance();
        bridge = new AsyncOperationBridge(LOGICAL_OPERATION_STEP_NAME);
    }

    @Test
    public void testResetCounter() throws Exception {
        bridge.resetCounter(contextSpy);
        verify(contextSpy).setVariable(bridge.getTryCounterContextVarName(), 0);
        assertEquals(0, bridge.getCounter(contextSpy));
    }

    @Test
    public void testIncrementCounter() throws Exception {
        contextSpy.setVariable(bridge.getTryCounterContextVarName(), 0);
        bridge.incrementCounter(contextSpy);
        verify(contextSpy).setVariable(bridge.getTryCounterContextVarName(), 1);
    }

    @Test
    public void testGetCounter() throws Exception {
        contextSpy.setVariable(bridge.getTryCounterContextVarName(), 0);
        bridge.getCounter(contextSpy);
        verify(contextSpy).getVariable(bridge.getTryCounterContextVarName());
    }

    @Test
    public void testSetOperationId() throws Exception {
        bridge.setOperationId(contextSpy, OPERATION_ID);
        verify(contextSpy).setVariable(eq(bridge.getOperationIdContextVarName()), contains(OPERATION_ID));
    }
}
