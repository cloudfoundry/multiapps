package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.slp.Constants;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Processes;
import com.sap.lmsl.slp.Service;
import com.sap.lmsl.slp.Services;

public class ActivitiService {

    protected final ServiceMetadata serviceMetadata;

    @Inject
    private ActivitiProcessFactory activitiProcessFactory;

    @Inject
    private ActivitiFacade activitiFacade;

    public ActivitiService(ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    public ServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }

    public static Services getServices(List<ActivitiService> activitiServices) {
        List<Service> services = new ArrayList<Service>();
        for (ActivitiService activitiService : activitiServices) {
            services.add(activitiService.getService());
        }
        return SlpObjectFactory.createServices(services);
    }

    public Service getService() {
        return SlpObjectFactory.createService(serviceMetadata.getId(), serviceMetadata.getDisplayName(), serviceMetadata.getDescription(),
            ActivitiParameter.getParameters(serviceMetadata.getParameters()));
    }

    public Processes getProcesses(String spaceId) {
        return ActivitiProcess.getProcesses(getActivitiProcesses(spaceId));
    }

    public Process getProcess(String spaceId, String processId) {
        ActivitiProcess activitiProcess = getActivitiProcess(spaceId, processId);
        return activitiProcess != null ? activitiProcess.getProcess() : null;
    }

    /**
     * Starts a new process instance with the specified user id and parameters.
     * 
     * @param userId user id for starting the process
     * @param parameters list of process parameters for starting the process
     * @return the newly started process
     */
    public Process startProcess(String spaceId, String userId, List<Parameter> parameters) throws SLException {
        return startActivitiProcess(spaceId, userId, parameters, new HashMap<String, Object>()).getProcess();
    }

    /**
     * Starts a new process instance with the specified user id, parameters and additional variables.
     * 
     * @param userId user id for starting the process
     * @param parameters list of process parameters for starting the process
     * @param additionalVariables list of additional variables to add them to the process context
     * @return the newly started process
     */
    public Process startProcess(String spaceId, String userId, List<Parameter> parameters, Map<String, Object> additionalVariables)
        throws SLException {
        return startActivitiProcess(spaceId, userId, parameters, additionalVariables).getProcess();
    }

    public ActivitiProcess getActivitiProcess(String spaceId, String processId) {
        ProcessInstance processInstance = getActivitiFacade().getActiveProcessInstance(serviceMetadata.getImplementationId(), spaceId,
            processId);
        if (processInstance != null && isInstanceOfCurrentService(processInstance.getId())) {
            return activitiProcessFactory.create(serviceMetadata, processInstance);
        }
        HistoricProcessInstance historicProcessInstance = getActivitiFacade().getHistoricProcessInstance(
            serviceMetadata.getImplementationId(), spaceId, processId);
        if (historicProcessInstance != null && isInstanceOfCurrentService(historicProcessInstance.getId())) {
            return activitiProcessFactory.create(serviceMetadata, historicProcessInstance);
        }
        return null;
    }

    private ActivitiProcess startActivitiProcess(String spaceId, String userId, List<Parameter> parameters,
        Map<String, Object> additionalVariables) throws SLException {
        Map<String, Object> variables = ActivitiParameter.getActivitiVariables(parameters, serviceMetadata.getParameters(), Boolean.TRUE,
            Boolean.TRUE);
        variables.put(Constants.VARIABLE_NAME_SERVICE_ID, serviceMetadata.getId());
        variables.put(Constants.VARIABLE_NAME_SPACE_ID, spaceId);
        variables.putAll(additionalVariables);
        ProcessInstance processInstance = getActivitiFacade().startProcessInstance(userId, serviceMetadata.getImplementationId(),
            variables);

        return activitiProcessFactory.create(serviceMetadata, processInstance);
    }

    private List<ActivitiProcess> getActivitiProcesses(String spaceId) {
        List<ProcessInstance> activeProcessInstances = getActiveProcessInstances(spaceId);
        List<HistoricProcessInstance> historicProcessInstances = getHistoricProcessInstances(spaceId, activeProcessInstances);
        List<ActivitiProcess> processes = new ArrayList<ActivitiProcess>();
        for (ProcessInstance processInstance : activeProcessInstances) {
            processes.add(activitiProcessFactory.create(serviceMetadata, processInstance));
        }
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            processes.add(activitiProcessFactory.create(serviceMetadata, historicProcessInstance));
        }
        return processes;
    }

    private List<HistoricProcessInstance> getHistoricProcessInstances(String spaceId, List<ProcessInstance> activeProcessInstances) {
        List<HistoricProcessInstance> historicProcessInstances = getActivitiFacade().getHistoricProcessInstances(
            this.serviceMetadata.getImplementationId(), spaceId);
        List<HistoricProcessInstance> result = new ArrayList<>();
        for (HistoricProcessInstance processInstance : historicProcessInstances) {
            if (contains(activeProcessInstances, processInstance.getId())) {
                continue;
            }
            if (!isInstanceOfCurrentService(processInstance.getId())) {
                continue;
            }
            result.add(processInstance);
        }
        return result;
    }

    private List<ProcessInstance> getActiveProcessInstances(String spaceId) {
        List<ProcessInstance> activeProcessInstances = getActivitiFacade().getActiveProcessInstances(
            this.serviceMetadata.getImplementationId(), spaceId);
        List<ProcessInstance> result = new ArrayList<>();
        for (ProcessInstance processInstance : activeProcessInstances) {
            String processInstanceId = processInstance.getId();
            if (!isInstanceOfCurrentService(processInstanceId)) {
                continue;
            }
            result.add(processInstance);
        }
        return result;
    }

    private boolean isInstanceOfCurrentService(String processInstanceId) {
        String processServiceId = getActivitiFacade().getServiceId(processInstanceId);
        return processServiceId != null ? processServiceId.equals(serviceMetadata.getId()) : false;
    }

    private static boolean contains(List<ProcessInstance> processInstances, String processId) {
        for (ProcessInstance process : processInstances) {
            if (process.getId().equals(processId)) {
                return true;
            }
        }
        return false;
    }

    private ActivitiFacade getActivitiFacade() {
        if (activitiFacade == null) {
            activitiFacade = ActivitiFacade.getInstance();
        }
        return activitiFacade;
    }

}
