package com.sap.activiti.common.deploy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DeployableTest {

    private static final String EXPECTED_KEY = "test.provision.hana.singlestep";
    private DeployableTestPO po;

    @Before
    public void setUp() {
        po = new DeployableTestPO();
    }

    @Test
    public void testKeyIsExtractedCorrectly() {
        Deployable installHanaDeployable = new Deployable("single_step.bpmn", "/com/sap/activiti/common/cfg/");
        assertEquals("The process definition key was not extracted correctly from the bpmn file.", EXPECTED_KEY,
            installHanaDeployable.getProcessDefinitionKey());
    }

    @Test
    public void testKeyIsExtractedCorrectlyWithSpacesInProcessTag() {
        assertEquals("The process definition key was not extracted correctly from the bpmn file.", EXPECTED_KEY,
            po.getDeployableWithSpaces().getProcessDefinitionKey());
    }

    @Test
    public void testKeyIsExtractedCorrectlyWithAlteredParameterOrder() {
        assertEquals("The process definition key was not extracted correctly from the bpmn file.", EXPECTED_KEY,
            po.getDeployableWithAlteredParameterOrder().getProcessDefinitionKey());
    }
}
