package com.sap.cloud.lm.sl.slp.util;

import java.io.IOException;
import java.text.MessageFormat;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.persistence.services.FileStorageException;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.services.ProcessLoggerProviderFactory;

public final class AbstractProcessComponentUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProcessComponentUtil.class);

    private AbstractProcessComponentUtil() {
    }

    public static void finalizeLogs(DelegateExecution context, ProcessLoggerProviderFactory processLoggerProviderFactory) {
        // Make sure that if the same thread is reused for a job of another process, it will get a
        // new logger:
        processLoggerProviderFactory.removeAll();
        // Write the log messages:
        try {
            processLoggerProviderFactory.flush(context, getLogDir());
        } catch (IOException | FileStorageException e) {
            logException(e);
        }
    }

    public static void appendLogs(DelegateExecution context, ProcessLoggerProviderFactory processLoggerProviderFactory) {
        // Make sure that if the same thread is reused for a job of another process, it will get a
        // new logger:
        processLoggerProviderFactory.removeAll();
        // Write the log messages:
        try {
            processLoggerProviderFactory.append(context, getLogDir());
        } catch (IOException | FileStorageException e) {
            logException(e);
        }
    }

    private static String getLogDir() {
        return ProcessLoggerProviderFactory.LOG_DIR;
    }

    private static void logException(Exception e) {
        LOGGER.warn(MessageFormat.format(Messages.COULD_NOT_PERSIST_LOGS_FILE, e.getMessage()), e);
    }

}
