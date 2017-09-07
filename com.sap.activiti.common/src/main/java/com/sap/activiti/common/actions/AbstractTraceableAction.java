package com.sap.activiti.common.actions;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.ArrayUtils;

import com.sap.activiti.common.Constants;

public abstract class AbstractTraceableAction implements IActivitiAction {

    private static final String ANONYMOUS_USER = "ANONYMOUS";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private String processInstanceId;
    private String reasonMessage;
    private String userName;

    public AbstractTraceableAction(String processInstanceId, String userId, String reasonMessage) {
        this.processInstanceId = processInstanceId;
        this.reasonMessage = reasonMessage;
        this.userName = resolveUserName(userId);
    }

    private String resolveUserName(String userId) {
		if (userId == null || userId.isEmpty()) {
			return ANONYMOUS_USER;
		}

		User user = getUserById(userId);
		if (user == null || user.getFirstName() == null || user.getLastName() == null) {
			return userId;
		}

		return String.format("%s %s", user.getFirstName(), user.getLastName());
	}

    protected User getUserById(String userId) {
        return getDefaultProcessEngine().getIdentityService().createUserQuery().userId(userId).singleResult();
    }

    protected String getProcessInstanceId() {
        return processInstanceId;
    }

    protected String getUserName() {
        return userName;
    }

    protected String getReasonMessage() {
        return reasonMessage;
    }

    protected ProcessEngine getDefaultProcessEngine() {
        return ProcessEngines.getDefaultProcessEngine();
    }
    
    protected ManagementService getManagementService() {
        return getDefaultProcessEngine().getManagementService();
     }

    public void logTracibilityInformation(String message) {
        RuntimeService runtimeService = getDefaultProcessEngine().getRuntimeService();

        String actionLog = String.format("[%s] %s\n", getCurrentTimestamp(), message);
        appendTracibilityInformationToContext(runtimeService, this.processInstanceId, Constants.ACTION_LOG, actionLog);

        String actionType = String.format("%s\n", getType().name().toUpperCase());
        appendTracibilityInformationToContext(runtimeService, this.processInstanceId, Constants.EXECUTED_ACTIONS_TYPE_LOG, actionType);
    }

    private void appendTracibilityInformationToContext(RuntimeService runtimeService, String processInstanceId, String contextKey,
        String information) {

        byte[] actionTypeLogBytes = (byte[]) runtimeService.getVariable(processInstanceId, contextKey);
        byte[] actionTypeBytes = information.getBytes(Charsets.UTF_8);
        actionTypeLogBytes = (actionTypeLogBytes == null) ? actionTypeBytes : ArrayUtils.addAll(actionTypeLogBytes, actionTypeBytes);
        runtimeService.setVariable(processInstanceId, contextKey, actionTypeLogBytes);
    }

    protected String getCurrentTimestamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return sdf.format(date);
    }
}
