package com.sap.activiti.common.actions;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Job;

import com.sap.activiti.common.impl.AbstractActivitiStep;

public class LogicalStepNameProvider {

    private ServiceTask serviceTask;
    private ProcessEngine processEngine;
    private Job job;

    public LogicalStepNameProvider(ProcessEngine processEngine, Job job) throws LogicalStepNameProviderException {
        this.processEngine = processEngine;
        this.job = job;
    }

    String getActivityId() {
        return processEngine.getRuntimeService()
            .createExecutionQuery()
            .executionId(job.getExecutionId())
            .singleResult()
            .getActivityId();
    }

    FlowElement getFlowElement(String activityId) {
        BpmnModel bpmnModel = processEngine.getRepositoryService()
            .getBpmnModel(job.getProcessDefinitionId());
        return bpmnModel.getMainProcess()
            .getFlowElement(activityId);
    }

    public String getLogicalStepName() throws LogicalStepNameProviderException {
        String logicalStepName = getLogicalStepNameFromModel();
        if (logicalStepName == null) {
            ServiceTask serviceTask = getServiceTask();
            if (isClass(serviceTask.getImplementationType())) {
                logicalStepName = getLogicalStepNameFromClass(serviceTask);
            }
            if (isDelegateExpression(serviceTask.getImplementationType())) {
                logicalStepName = getLogicalStepNameFromDelegateExpression(serviceTask);
            }
        }
        return logicalStepName;
    }

    private String getLogicalStepNameFromClass(final ServiceTask serviceTask) throws LogicalStepNameProviderException {
        String className = serviceTask.getImplementation();
        try {
            Class<? extends AbstractActivitiStep> stepClass = Class.forName(className)
                .asSubclass(AbstractActivitiStep.class);
            return stepClass.newInstance()
                .getLogicalStepName();
        } catch (ClassNotFoundException e) {
            throw new LogicalStepNameProviderException("Cannot instantiate activity class", e);
        } catch (InstantiationException e) {
            throw new LogicalStepNameProviderException("Cannot instantiate activity class", e);
        } catch (IllegalAccessException e) {
            throw new LogicalStepNameProviderException("Cannot instantiate activity class", e);
        } catch (ClassCastException e) {
            throw new LogicalStepNameProviderException("Unsupported activity class " + className, e);
        }
    }

    private String getLogicalStepNameFromDelegateExpression(final ServiceTask serviceTask) throws LogicalStepNameProviderException {
        ProcessEngineConfigurationImpl previousProcessEngineConfiguration = Context.getProcessEngineConfiguration();
        try {
            return processEngine.getManagementService()
                .executeCommand((new Command<String>() {

                    @Override
                    public String execute(CommandContext context) {
                        ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) processEngine)
                            .getProcessEngineConfiguration();
                        Context.setProcessEngineConfiguration(processEngineConfiguration);
                        Expression expression = processEngineConfiguration.getExpressionManager()
                            .createExpression(serviceTask.getImplementation());
                        ExecutionEntity executionn = (ExecutionEntity) processEngine.getRuntimeService()
                            .createProcessInstanceQuery()
                            .processInstanceId(job.getProcessInstanceId())
                            .singleResult();
                        return ((AbstractActivitiStep) expression.getValue(executionn)).getLogicalStepName();
                    }

                }));
        } catch (ClassCastException e) {
            throw new LogicalStepNameProviderException("Unsupported delegate expression " + serviceTask.getImplementation(), e);
        } finally {
            Context.setProcessEngineConfiguration(previousProcessEngineConfiguration);
        }
    }

    private String getLogicalStepNameFromModel() throws LogicalStepNameProviderException {
        for (FieldExtension field : getServiceTask().getFieldExtensions()) {
            if ("logicalStepName".equals(field.getFieldName())) {
                return field.getStringValue();
            }
        }
        return null;
    }

    ServiceTask getServiceTask() throws LogicalStepNameProviderException {
        if (serviceTask == null) {
            String activityId = getActivityId();
            FlowElement flowElement = getFlowElement(activityId);
            if (!(flowElement instanceof ServiceTask)) {
                throw new LogicalStepNameProviderException("JobId does not refer to a servicetask");
            }

            ServiceTask serviceTask = (ServiceTask) flowElement;
            validateServiceTaskImplementationType(serviceTask.getImplementationType());
            this.serviceTask = serviceTask;
        }
        return serviceTask;
    }

    private void validateServiceTaskImplementationType(String implementationType) throws LogicalStepNameProviderException {
        if (!isClass(implementationType) && !isDelegateExpression(implementationType)) {
            throw new LogicalStepNameProviderException("Unsupported implementation type: " + implementationType);
        }
    }

    private boolean isDelegateExpression(String implementationType) {
        return implementationType.equals(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
    }

    private boolean isClass(String implementationType) {
        return implementationType.equals(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
    }

}
