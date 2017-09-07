package com.sap.activiti.common.util.impl;

import org.activiti.engine.delegate.DelegateExecution;

import com.sap.activiti.common.util.ISkipHelper;

public class DelegateExecutionSkipHelper extends AbstractSkipHelper implements ISkipHelper {

	private DelegateExecution context;

	public DelegateExecutionSkipHelper(DelegateExecution context) {
		this.context = context;
	}

	@Override
	protected Object getVariable(String variableName) {
		return context.getVariable(variableName);
	}

	@Override
	protected void setVariable(String variableName, Object value) {
		context.setVariable(variableName, value);
	}

}
