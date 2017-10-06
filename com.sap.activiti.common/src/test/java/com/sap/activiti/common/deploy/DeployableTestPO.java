package com.sap.activiti.common.deploy;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class DeployableTestPO {

    public Deployable getDeployableWithSpaces() {
        return new Deployable("single_step.bpmn", "/com/sap/activiti/common/cfg/") {
            @Override
            public InputStream getBpmnStream() {
                InputStream regularInputStream = super.getBpmnStream();
                return getAlteredBpmnStream(regularInputStream, "<process ", "<process                   ");
            }
        };
    }

    public Deployable getDeployableWithAlteredParameterOrder() {
        return new Deployable("single_step.bpmn", "/com/sap/activiti/common/cfg/") {
            @Override
            public InputStream getBpmnStream() {

                InputStream regularInputStream = super.getBpmnStream();

                return getAlteredBpmnStream(regularInputStream, "<process[^>]*?>",
                    "<process name=\"Single Step Test Process\" id=\"test.provision.hana.singlestep\"  isExecutable=\"true\">");
            }
        };
    }

    private InputStream getAlteredBpmnStream(InputStream regularInputStream, String patternToSearch, String patternToReplaceWith) {
        InputStream streamWithAddedSpaces = null;
        try {
            String bpmn = IOUtils.toString(regularInputStream);

            bpmn = bpmn.replaceFirst(patternToSearch, patternToReplaceWith);

            streamWithAddedSpaces = new ByteArrayInputStream(bpmn.getBytes("UTF-8"));
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }

        return streamWithAddedSpaces;
    }

}
