package com.sap.activiti.common;

import org.activiti.engine.EngineServices;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LoggerTest {

    private Logger logger;
    private org.slf4j.Logger loggerImpl;
    private DelegateExecution context;

    @Before
    public void init() {
        logger = Logger.getInstance(LoggerTest.class);
        loggerImpl = Mockito.mock(org.slf4j.Logger.class);
        Mockito.when(loggerImpl.isDebugEnabled())
            .thenReturn(true);
        logger.setLoggerImpl(loggerImpl);

        EngineServices engineServices = Mockito.mock(EngineServices.class);
        context = Mockito.mock(DelegateExecution.class);
        Mockito.when(context.getEngineServices())
            .thenReturn(engineServices);
        Mockito.when(context.getProcessDefinitionId())
            .thenReturn("PD_ID");
        Mockito.when(context.getProcessInstanceId())
            .thenReturn("PI_ID");
        Mockito.when(context.getCurrentActivityId())
            .thenReturn("CA_ID");
    }

    @Test
    public void testDebugWithNullContext() {
        logger.debug(null, "Hello {}", "world");
        Mockito.verify(loggerImpl)
            .debug("[{}][{}][{}] Hello {}", null, null, null, "world");
    }

    @Test
    public void testDebugWithoutReplacements() {
        logger.debug(context, "Hello world");
        Mockito.verify(loggerImpl)
            .debug("[{}][{}][{}] Hello world", "PD_ID", "PI_ID", "CA_ID");
    }

    @Test
    public void testDebugWithOneReplacement() {
        logger.debug(context, "Hello world {}", "again");
        Mockito.verify(loggerImpl)
            .debug("[{}][{}][{}] Hello world {}", "PD_ID", "PI_ID", "CA_ID", "again");
    }

    @Test
    public void testDebugWithMultipleReplacement() {
        logger.debug(context, "Hello world {}. {}+{} is {}", "again", 1, "1", 2);
        Mockito.verify(loggerImpl)
            .debug("[{}][{}][{}] Hello world {}. {}+{} is {}", "PD_ID", "PI_ID", "CA_ID", "again", 1, "1", 2);
    }

    @Test
    public void testErrorWithThrowable() {
        IllegalArgumentException e = new IllegalArgumentException();
        logger.error(context, "Execution failed", e);
        Mockito.verify(loggerImpl)
            .error("[{}][{}][{}] Execution failed", "PD_ID", "PI_ID", "CA_ID", e);
    }

    @Test
    public void testErrorWithReplacements() {
        IllegalArgumentException e = new IllegalArgumentException();
        logger.error(context, "Execution failed for methods {} and {}", "one", "two", e);
        Mockito.verify(loggerImpl)
            .error("[{}][{}][{}] Execution failed for methods {} and {}", "PD_ID", "PI_ID", "CA_ID", "one", "two", e);
    }

    @Test
    public void testWhenNotInDebugMode() {
        Mockito.when(loggerImpl.isDebugEnabled())
            .thenReturn(false);
        logger.debug(context, "This shold {} be logged", "NOT");
        Mockito.verify(loggerImpl)
            .isDebugEnabled();
        Mockito.verifyNoMoreInteractions(loggerImpl);
    }

}
