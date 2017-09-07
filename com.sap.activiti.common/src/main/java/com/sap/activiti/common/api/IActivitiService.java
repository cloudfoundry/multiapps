package com.sap.activiti.common.api;

import java.io.IOException;

import org.activiti.engine.ProcessEngine;

public interface IActivitiService {

    /**
     * Initialization consists of few things:
     * <ol>
     * <li>Create Activiti process engine with configuration specified into the provided IActivitiConfiguration instance</li>
     * <li>Deploy BPMN files into Activiti engine. Files are deployed only if their content is different than the content of the latest
     * deployed BPMN process with the same name into Activiti.</li>
     * </ol>
     *
     * @return the created Activiti process engine
     * @throws IOException
     */
    ProcessEngine initialize(IActivitiConfiguration activitiConfiguration) throws IOException;

}
