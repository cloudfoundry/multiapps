package com.sap.activiti.common.impl;

import static com.sap.activiti.common.Constants.CHARSET_UTF_8;
import static com.sap.activiti.common.Constants.LOGICAL_STEP_RETRY_MSG_SUFFIX;

import java.io.UnsupportedEncodingException;

import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.ExecutionStatus;
import com.sap.activiti.common.Logger;
import com.sap.activiti.common.LogicalRetryException;
import com.sap.activiti.common.api.IAsyncOperationBridge;
import com.sap.activiti.common.util.ISkipHelper;

public abstract class AbstractActivitiStepWithBridge extends AbstractActivitiStep {

    private static final Logger LOGGER = Logger.getInstance(AbstractActivitiStepWithBridge.class);

    private IAsyncOperationBridge bridge;

    public IAsyncOperationBridge getBridge() {
        if (bridge == null) {
            bridge = new AsyncOperationBridge(getLogicalStepName());
        }
        return bridge;
    }

    @Override
    protected void dropSkipRequestWhenDone(DelegateExecution context) {
        if (shouldDropSkipRequest()) {
            super.dropSkipRequestWhenDone(context);
        }
    }

    /**
     * The skip request of bridged steps cannot be dropped automatically. Only the polling step should drop the request and there is no
     * simple way to check if this is the polling step. <br>
     * <br>
     * You can override this method in your custom polling step implementation and use it to tell the step that this is the polling step of
     * the two and that the skip request can be dropped.<br>
     * <br>
     * You can also do it outside of the bridged steps by using the {@link ISkipHelper} interface manually.
     */
    protected boolean shouldDropSkipRequest() {
        return false;
    }

    @Override
    protected ExecutionStatus executeStep(DelegateExecution context) throws Exception {
        failTaskIfRetryIsNeeded(context);
        ExecutionStatus status;
        HttpLogger log = HttpLogger.getInstance();
        try {
            log.enableLogCollection();
            status = pollStatus(context);
            log.persistLog(getLogicalStepName(), context);
            clearRetryMessage(status, context);
        } finally {
            log.disableLogCollection();
        }
        return status;
    }

    protected abstract ExecutionStatus pollStatus(DelegateExecution context) throws Exception;

    protected void setRetryMessage(DelegateExecution context, String retryMessage) {
        try {
            context.setVariable(getRetryMessageVariable(), retryMessage.getBytes(CHARSET_UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void failTaskIfRetryIsNeeded(DelegateExecution context) {
        if (!isInLogicalRetry(context)) {
            return;
        }
        LOGGER.debug(context, "Task must be retried");
        String retryMessage = getRetryMessage(context);
        throw new LogicalRetryException(retryMessage);
    }

    private boolean isInLogicalRetry(DelegateExecution context) {
        String status = (String) context.getVariable(getStatusVariable());
        return ExecutionStatus.LOGICAL_RETRY.name()
            .equalsIgnoreCase(status);
    }

    private String getRetryMessage(DelegateExecution context) {
        byte[] retryMsg = (byte[]) context.getVariable(getRetryMessageVariable());
        if (retryMsg == null || retryMsg.length == 0) {
            return "No retry message available";
        }
        try {
            return new String(retryMsg, CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void clearRetryMessage(ExecutionStatus status, DelegateExecution context) {
        if (!ExecutionStatus.LOGICAL_RETRY.equals(status)) {
            context.removeVariable(getRetryMessageVariable());
        }
    }

    String getRetryMessageVariable() {
        return getLogicalStepName() + LOGICAL_STEP_RETRY_MSG_SUFFIX;
    }

}
