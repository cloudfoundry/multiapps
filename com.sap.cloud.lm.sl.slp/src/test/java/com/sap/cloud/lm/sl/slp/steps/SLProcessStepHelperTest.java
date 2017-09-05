package com.sap.cloud.lm.sl.slp.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;

import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.impl.MockDelegateExecution;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.Constants;
import com.sap.cloud.lm.sl.slp.services.ProcessLoggerProviderFactory;

@RunWith(Parameterized.class)
public class SLProcessStepHelperTest {

    private static final String PROCESS_INSTANCE_ID = "process-instance-id";
    private static final String STEP = "current-activiti-id";

    private DelegateExecution contextMock = MockDelegateExecution.createSpyInstance();

    private StepInput input;
    private StepOutput output;

    @Mock
    private ProcessLoggerProviderFactory processLoggerProviderFactory;

    public SLProcessStepHelperTest(StepInput input, StepOutput output) {
        this.input = input;
        this.output = output;
    }

    private static class StepInput {
        private final boolean isInError;
        private final int previousTaskId;
        private final ExecutionStatus initialStatus;
        private final String retryStepName;

        public StepInput(boolean isInError, int previousTaskId, ExecutionStatus initialStatus, String retryStepName) {
            super();
            this.isInError = isInError;
            this.previousTaskId = previousTaskId;
            this.initialStatus = initialStatus;
            this.retryStepName = retryStepName;
        }
    }

    private static class StepOutput {
        private final String expectedIndexedStepName;
        private final boolean removeMessagesForCurrentStep;
        private final boolean removeMessagesForRetryStep;
        private final String indexedRetryStepName;
        private final boolean removeRetryStepVariable;

        public StepOutput(String expectedIndexedStepName, boolean removeMessagesForCurrentStep, boolean removeMessagesForRetryStep,
            String indexedRetryStepName, boolean removeRetryStepVariable) {
            super();
            this.expectedIndexedStepName = expectedIndexedStepName;
            this.removeMessagesForCurrentStep = removeMessagesForCurrentStep;
            this.removeMessagesForRetryStep = removeMessagesForRetryStep;
            this.indexedRetryStepName = indexedRetryStepName;
            this.removeRetryStepVariable = removeRetryStepVariable;
        }
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // 0: New step without previous step with same step name
            { new StepInput(false, -1, ExecutionStatus.NEW, null), new StepOutput(STEP + 0, false, false, null, false) },
            // 1: New step with previous step with same step name
            { new StepInput(false, 0, ExecutionStatus.NEW, null), new StepOutput(STEP + 1, false, false, null, false) },
            // 2: Step with error with previous step with same step name
            { new StepInput(true, 0, ExecutionStatus.NEW, null), new StepOutput(STEP + 0, true, false, null, false) },
            // 3: Step in LOGICAL_RETRY status
            { new StepInput(false, 0, ExecutionStatus.LOGICAL_RETRY, null), new StepOutput(STEP + 0, false, false, null, false) },
            // 4: Step in RUNNING status
            { new StepInput(false, 0, ExecutionStatus.RUNNING, null), new StepOutput(STEP + 0, false, false, null, false) },
            // 5: Step in error with corresponding retry step name
            { new StepInput(true, 0, ExecutionStatus.NEW, "retry-step"), new StepOutput(STEP + 0, true, true, "retry-step0", true) }, });
    }

    @Test
    public void test() throws Exception {
        when(contextMock.getVariable("correlationId")).thenReturn(PROCESS_INSTANCE_ID);
        when(contextMock.getCurrentActivityId()).thenReturn(STEP);
        if (input.retryStepName != null) {
            when(contextMock.hasVariable(Constants.RETRY_STEP_NAME)).thenReturn(true);
            when(contextMock.getVariable(Constants.RETRY_STEP_NAME)).thenReturn(input.retryStepName);
        }
        SLProcessStepHelper helper = new SLProcessStepHelperMock(getProgressMessageService(), processLoggerProviderFactory);

        helper.preExecuteStep(contextMock, input.initialStatus);

        assertEquals(output.expectedIndexedStepName, helper.getIndexedStepName());
        verify(contextMock).setVariable(Constants.INDEXED_STEP_NAME, output.expectedIndexedStepName);
        if (output.removeMessagesForCurrentStep) {
            verify(helper.getProgressMessageService()).removeByProcessIdAndTaskId(PROCESS_INSTANCE_ID, output.expectedIndexedStepName);
        } else {
            verify(helper.getProgressMessageService(), never()).removeByProcessIdAndTaskId(PROCESS_INSTANCE_ID,
                output.expectedIndexedStepName);
        }
        if (output.removeMessagesForRetryStep) {
            verify(helper.getProgressMessageService()).removeByProcessIdAndTaskId(PROCESS_INSTANCE_ID, output.indexedRetryStepName);
        } else {
            verify(helper.getProgressMessageService(), never()).removeByProcessIdAndTaskId(PROCESS_INSTANCE_ID,
                output.indexedRetryStepName);
        }
        if (output.removeRetryStepVariable) {
            verify(contextMock).removeVariable(Constants.RETRY_STEP_NAME);
        } else {
            verify(contextMock, never()).removeVariable(Constants.RETRY_STEP_NAME);
        }
    }

    private class SLProcessStepHelperMock extends SLProcessStepHelper {

        public SLProcessStepHelperMock(ProgressMessageService progressMessageService,
            ProcessLoggerProviderFactory processLoggerProviderFactory) {
            super(progressMessageService, processLoggerProviderFactory, new StepIndexProvider() {

                @Override
                public Integer getDefaultStepIndex(DelegateExecution context) {
                    return -1;
                }
            });
        }

        @Override
        Job getJob(DelegateExecution context) {
            Job testJob = mock(Job.class);
            if (input.isInError) {
                when(testJob.getExceptionMessage()).thenReturn("Test exception messages!");
            } else {
                when(testJob.getExceptionMessage()).thenReturn(null);
            }
            return testJob;
        }
    }

    private ProgressMessageService getProgressMessageService() {
        ProgressMessageService progressMessageService = mock(ProgressMessageService.class);
        try {
            if (input.previousTaskId > -1) {
                when(progressMessageService.findLastTaskId(PROCESS_INSTANCE_ID, STEP)).thenReturn(STEP + input.previousTaskId);
            } else {
                when(progressMessageService.findLastTaskId(PROCESS_INSTANCE_ID, STEP)).thenReturn(null);
            }
        } catch (SLException e) {
            fail(e.getMessage());
        }
        return progressMessageService;
    }
}
