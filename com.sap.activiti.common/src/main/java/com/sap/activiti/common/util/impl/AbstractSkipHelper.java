package com.sap.activiti.common.util.impl;

import com.sap.activiti.common.util.ISkipHelper;

public abstract class AbstractSkipHelper implements ISkipHelper {

	protected static final String SKIP_STEP_PREFIX = "Skip_";

	protected abstract Object getVariable(String variableName);

	protected abstract void setVariable(String variableName, Object value);

	@Override
	public boolean hasSkipRequest(String logicalStepName) {
		return SkipRequest.SKIP.toString().equals(getVariable(getSkipVariable(logicalStepName)));
	}

	@Override
	public void createSkipRequest(String logicalStepName) {
		setVariable(getSkipVariable(logicalStepName), SkipRequest.SKIP.toString());
	}

	@Override
	public void removeSkipRequest(String logicalStepName) {
		if (hasSkipRequest(logicalStepName)) {
			setVariable(getSkipVariable(logicalStepName), SkipRequest.NONE.toString());
		}
	}

	protected String getSkipVariable(String logicalStepName) {
		return SKIP_STEP_PREFIX + logicalStepName;
	}

}
