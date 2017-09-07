package com.sap.activiti.common;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.TransactionContext;
import org.activiti.engine.impl.cfg.TransactionListener;
import org.activiti.engine.impl.cfg.TransactionState;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.JobEntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoJobRetryCommandFactoryTest {

    private static final int RETRY_COUNT = 0;
    private static final String JOB_FAILURE_MESSAGE = "Job failed";
    private static final String JOB_ID = "1234";

    private CommandContext comamndContextMock;
    private ProcessEngineConfigurationImpl processEngineConfigurationImplMock;
    private JobEntityManager jobEntityManagerMock;
    private TransactionContext transactionContextMock;

    private JobEntity jobEntity;
    private Command<Object> classUnderTest;

    private CommandContext originalComamndContext;
    private ProcessEngineConfigurationImpl originalProcessEngineConfigurationImpl;

    @Before
    public void setup() {
        jobEntity = mock(JobEntity.class);
        comamndContextMock = mock(CommandContext.class);
        processEngineConfigurationImplMock = mock(ProcessEngineConfigurationImpl.class);
        jobEntityManagerMock = mock(JobEntityManager.class);
        transactionContextMock = mock(TransactionContext.class);

        when(comamndContextMock.getJobEntityManager()).thenReturn(jobEntityManagerMock);
        when(jobEntityManagerMock.findJobById(JOB_ID)).thenReturn(jobEntity);
        when(comamndContextMock.getTransactionContext()).thenReturn(transactionContextMock);

        originalComamndContext = Context.getCommandContext();
        originalProcessEngineConfigurationImpl = Context.getProcessEngineConfiguration();

        Context.setCommandContext(comamndContextMock);
        Context.setProcessEngineConfiguration(processEngineConfigurationImplMock);
    }

    @After
    public void tearDown() {
        Context.setCommandContext(originalComamndContext);
        Context.setProcessEngineConfiguration(originalProcessEngineConfigurationImpl);
    }

    @Test
    public void testJobEntitySetupWhenExceptionIsPresent() {
        classUnderTest = new NoJobRetryCommandFactory().getCommand(JOB_ID, new Exception(JOB_FAILURE_MESSAGE));

        classUnderTest.execute(comamndContextMock);

        verify(jobEntity).setExceptionMessage(contains(JOB_FAILURE_MESSAGE));

        verifyCommonBehaviour();
    }

    @Test
    public void testJobEntitySetupWhenExceptionIsNull() {
        classUnderTest = new NoJobRetryCommandFactory().getCommand(JOB_ID, null);

        classUnderTest.execute(comamndContextMock);

        verify(jobEntity, never()).setExceptionMessage(anyString());

        verifyCommonBehaviour();
    }

    private void verifyCommonBehaviour() {
        verify(jobEntity).setRetries(RETRY_COUNT);
        verify(transactionContextMock).addTransactionListener(eq(TransactionState.COMMITTED), any(TransactionListener.class));
    }
}
