package com.sap.activiti.common.actions;

import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sap.activiti.common.EmptyActivitiStep;

public class LogicalStepNameProviderTest {

    private ServiceTask serviceTask;

    @Before
    public void setUp() {
        serviceTask = new ServiceTask();
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
    }

    @Test
    public void testModelWithLogicalStepName() throws Exception {
        serviceTask.getFieldExtensions()
            .add(createFieldExtension("logicalStepName", "TestStep"));
        serviceTask.getFieldExtensions()
            .add(createFieldExtension("testField", "something"));
        LogicalStepNameProvider provider = new TestableLogicalStepNameProvider(serviceTask);
        Assert.assertEquals("TestStep", provider.getLogicalStepName());
    }

    @Test
    public void testModelWithoutLogicalStepName() throws Exception {
        serviceTask.setImplementation(EmptyActivitiStep.class.getName());
        LogicalStepNameProvider provider = new TestableLogicalStepNameProvider(serviceTask);
        Assert.assertEquals("EmptyActivitiStep", provider.getLogicalStepName());
    }

    @Test(expected = LogicalStepNameProviderException.class)
    public void testUnsupportedActivityClass() throws Exception {
        serviceTask.setImplementation(DelegateExecution.class.getName());
        new TestableLogicalStepNameProvider(serviceTask).getLogicalStepName();
    }

    @Test(expected = LogicalStepNameProviderException.class)
    public void testUnsupportedImplementationType() throws Exception {
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        new TestableLogicalStepNameProvider(serviceTask).getLogicalStepName();
    }

    private FieldExtension createFieldExtension(String name, String stringValue) {
        FieldExtension field = new FieldExtension();
        field.setFieldName(name);
        field.setStringValue(stringValue);
        return field;
    }

    private static class TestableLogicalStepNameProvider extends LogicalStepNameProvider {

        private FlowElement flowElement;

        public TestableLogicalStepNameProvider(FlowElement serviceTask) throws LogicalStepNameProviderException {
            super(null, null);
            this.flowElement = serviceTask;
        }

        @Override
        String getActivityId() {
            return "activityId";
        }

        @Override
        FlowElement getFlowElement(String activityId) {
            return flowElement;
        }
    }

}
