package com.sap.activiti.common.util.impl;

import org.activiti.engine.EngineServices;

public class EngineServicesSkipHelper extends AbstractSkipHelper {

	private EngineServices engine;
	private String processInstanceId;

	public EngineServicesSkipHelper(EngineServices engine, String processInstanceId) {
		this.engine = engine;
		this.processInstanceId = processInstanceId;
	}

	@Override
	protected Object getVariable(String variableName) {
		return engine.getRuntimeService().getVariable(processInstanceId, variableName);
	}

	@Override
	protected void setVariable(String variableName, Object value) {
		engine.getRuntimeService().setVariable(processInstanceId, variableName, value);
	}

}
