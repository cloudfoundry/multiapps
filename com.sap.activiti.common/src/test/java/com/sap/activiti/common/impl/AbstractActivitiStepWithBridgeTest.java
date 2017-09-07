package com.sap.activiti.common.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.UnsupportedEncodingException;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.activiti.common.Constants;
import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.LogicalRetryException;

public class AbstractActivitiStepWithBridgeTest {

    private static final String EXCEPTION_MESSAGE_TEXT = "Exception message text";
    private AbstractActivitiStepWithBridge classUnderTest = null;
    private ExecutionStatus executionStatus = null;
    private DelegateExecution context = null;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {

        context = new MockDelegateExecution();
        executionStatus = ExecutionStatus.SUCCESS;
        classUnderTest = new AbstractActivitiStepWithBridge() {
            @Override
            protected ExecutionStatus pollStatus(DelegateExecution context) throws Exception {
                return executionStatus;
            }
        };
    }

    @Test
    public void testExceptionIsThrownWhenStatusIsLogicalRetry() throws Exception {
        expectedException.expect(LogicalRetryException.class);
        setLogicalRetryInContext();

        classUnderTest.executeStep(context);
    }

    @Test
    public void testExceptionHasCorrectMessage() throws Exception {
        expectedException.expect(LogicalRetryException.class);
        expectedException.expectMessage(EXCEPTION_MESSAGE_TEXT);
        setLogicalRetryInContext();

        classUnderTest.executeStep(context);
    }

    @Test
    public void testMessageIsCleanedAfterExecution() throws Exception {
        String msgVariableName = classUnderTest.getLogicalStepName() + Constants.LOGICAL_STEP_RETRY_MSG_SUFFIX;
        context.setVariable(msgVariableName, EXCEPTION_MESSAGE_TEXT.getBytes("UTF-8"));

        classUnderTest.executeStep(context);

        assertFalse(context.hasVariable(msgVariableName));
    }

    @Test
    public void testTaskIsExecutedIfStatusIsNull() throws Exception {
        ExecutionStatus status = classUnderTest.executeStep(context);

        assertEquals(ExecutionStatus.SUCCESS.name(), status.name());
    }

    @Test
    public void testTaskIsExecutedIfStatusIsNotLogicalRetry() throws Exception {
        ExecutionStatus status = classUnderTest.executeStep(context);

        assertEquals(ExecutionStatus.SUCCESS.name(), status.name());
    }

    private void setLogicalRetryInContext() throws UnsupportedEncodingException {
        context.setVariable(Constants.STEP_NAME_PREFIX + classUnderTest.getLogicalStepName(), ExecutionStatus.LOGICAL_RETRY.name());

        context.setVariable(classUnderTest.getLogicalStepName() + Constants.LOGICAL_STEP_RETRY_MSG_SUFFIX,
            EXCEPTION_MESSAGE_TEXT.getBytes("UTF-8"));
    }
}
