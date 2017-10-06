package com.sap.activiti.common.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;

import com.sap.activiti.common.api.IActivitiConfiguration;

public class BpmnDeployer {

    private List<Deployable> deployables;

    public BpmnDeployer(IActivitiConfiguration config) {
        this(config.getDeployables());
    }

    public BpmnDeployer(List<Deployable> deployables) {
        this.deployables = deployables;
    }

    public void deploy(RepositoryService repositoryService) throws IOException {
        for (Deployable deployable : deployables) {
            deploy(deployable, repositoryService);
        }
    }

    private void deploy(Deployable deployable, RepositoryService repositoryService) throws IOException {
        String lastBpmn = getLastBpmn(deployable, repositoryService);
        String newBpmn = readStreamAndCloseIt(deployable.getBpmnStream());

        if (!newBpmn.equals(lastBpmn)) {
            repositoryService.createDeployment().addString(deployable.getBpmnFileName(), newBpmn).deploy();
        }
    }

    private String getLastBpmn(Deployable deployable, RepositoryService repositoryService) throws IOException {
        ProcessDefinition lastProcessDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(
                deployable.getProcessDefinitionKey()).latestVersion().singleResult();

        if (lastProcessDefinition == null) {
            return null;
        }

        return readStreamAndCloseIt(repositoryService.getProcessModel(lastProcessDefinition.getId()));
    }

    private String readStreamAndCloseIt(InputStream is) throws IOException {
        try {
            return IOUtils.toString(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
