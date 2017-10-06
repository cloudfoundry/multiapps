package com.sap.activiti.common;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.LoggerFactory;

public class Logger {

    private static final String INTERNAL_MSG = "[{}][{}][{}] ";
    private static final int INTERNAL_ARGUMENTS_COUNT = 3;

    private org.slf4j.Logger loggerImpl;

    public static Logger getInstance(Class<?> clazz) {
        return new Logger(clazz);
    }

    public void debug(DelegateExecution context, String msg) {
        debug(context, msg, (Object[]) null);
    }

    public void debug(DelegateExecution context, String msg, Object arg) {
        debug(context, msg, new Object[] { arg });
    }

    public void debug(DelegateExecution context, String msg, Object... arguments) {
        if (loggerImpl.isDebugEnabled()) {
            loggerImpl.debug(INTERNAL_MSG + msg, getAllArguments(context, arguments));
        }
    }

    public void error(DelegateExecution context, String msg, Throwable throwable) {
        error(context, msg, new Object[] { throwable });
    }

    public void error(DelegateExecution context, String msg, Object... arguments) {
        loggerImpl.error(INTERNAL_MSG + msg, getAllArguments(context, arguments));
    }

    protected void setLoggerImpl(org.slf4j.Logger loggerImpl) {
        this.loggerImpl = loggerImpl;
    }

    private Logger(Class<?> clazz) {
        loggerImpl = LoggerFactory.getLogger(clazz);
    }

    private Object[] getAllArguments(DelegateExecution context, Object... arguments) {
        int argumentsCount = arguments != null ? arguments.length : 0;

        Object[] allArguments = new Object[argumentsCount + INTERNAL_ARGUMENTS_COUNT];

        if (arguments != null) {
            System.arraycopy(arguments, 0, allArguments, INTERNAL_ARGUMENTS_COUNT, arguments.length);
        }

        populateInternalArguments(context, allArguments);
        return allArguments;
    }

    private void populateInternalArguments(DelegateExecution context, Object[] allArguments) {
        if (context != null && context.getEngineServices() != null) {
            int ndx = 0;
            allArguments[ndx++] = context.getProcessDefinitionId();
            allArguments[ndx++] = context.getProcessInstanceId();
            allArguments[ndx++] = context.getCurrentActivityId();
        }
    }

}
