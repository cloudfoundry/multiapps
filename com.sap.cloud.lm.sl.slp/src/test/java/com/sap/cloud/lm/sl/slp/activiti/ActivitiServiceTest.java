package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.slp.TestUtils;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata.ParameterType;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.services.TaskExtensionService;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Service;
import com.sap.lmsl.slp.Services;

public class ActivitiServiceTest {

    @Mock
    private ActivitiFacade activitiFacade;

    @Mock
    private ActivitiProcessFactory activitiProcessFactory;

    private static final String PARAM_SID = "SID";
    private final String idOfCurrentService = "xsa-deploy";
    private final String idOfAnotherService = "cts-deploy";
    private final String implementationId = "deploy";
    // Parameters for the service:
    private Set<ParameterMetadata> parametersMetadata = new HashSet<ParameterMetadata>();

    private final String userId = "username";
    private final String spaceId = TestUtils.generateRandomString();

    private ServiceMetadata serviceMetadata = TestUtils.mockServiceMetadata(idOfCurrentService, implementationId, parametersMetadata);

    @InjectMocks
    private ActivitiService activitiService = new ActivitiService(serviceMetadata);

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        parametersMetadata.add(ParameterMetadata.builder().id(PARAM_SID).required(true).type(ParameterType.STRING).build());
    }

    @Test
    public void testGetServices() {
        Services services = ActivitiService.getServices(ListUtil.asList(mock(ActivitiService.class)));
        assertEquals(1, services.getService().size());
    }

    @Test
    public void testGetActiveActivitiProcess1() {
        String processInstanceId = TestUtils.generateRandomString();

        ProcessInstance processInstance = prepareActiveProcessInstance(idOfCurrentService, implementationId, processInstanceId);
        when(activitiFacade.getActiveProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(processInstance);

        ActivitiProcess activitiProcess = activitiService.getActivitiProcess(spaceId, processInstanceId);

        assertNotNull(activitiProcess);
        assertTrue(activitiProcess instanceof ActiveActivitiProcess);
        assertEquals(processInstance.getActivityId(), activitiProcess.getCurrentActivityId());
        assertEquals(processInstance.getId(), activitiProcess.getProcessInstanceId());
        assertEquals(processInstance.getProcessDefinitionId(), activitiProcess.getServiceMetadata().getImplementationId());
    }

    @Test
    public void testGetActiveActivitiProcess2() {
        String processInstanceId = TestUtils.generateRandomString();

        ProcessInstance processInstance = prepareActiveProcessInstance(idOfAnotherService, implementationId, processInstanceId);
        when(activitiFacade.getActiveProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(processInstance);

        ActivitiProcess activitiProcess = activitiService.getActivitiProcess(spaceId, processInstanceId);

        assertNull(activitiProcess);
    }

    @Test
    public void testGetHistoricActivitiProcess1() {
        String processInstanceId = TestUtils.generateRandomString();

        HistoricProcessInstance historicInstance = prepareHistoricProcessInstance(idOfCurrentService, implementationId, processInstanceId);
        when(activitiFacade.getHistoricProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(historicInstance);

        ActivitiProcess activitiProcess = activitiService.getActivitiProcess(spaceId, processInstanceId);

        assertNotNull(activitiProcess);
        assertTrue(activitiProcess instanceof FinishedActivitiProcess);
        assertEquals(historicInstance.getId(), activitiProcess.getProcessInstanceId());
        assertEquals(historicInstance.getProcessDefinitionId(), activitiProcess.getServiceMetadata().getImplementationId());
    }

    @Test
    public void testGetHistoricActivitiProcess2() {
        String processInstanceId = TestUtils.generateRandomString();

        HistoricProcessInstance historicInstance = prepareHistoricProcessInstance(idOfAnotherService, implementationId, processInstanceId);
        when(activitiFacade.getHistoricProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(historicInstance);

        ActivitiProcess activitiProcess = activitiService.getActivitiProcess(spaceId, processInstanceId);

        assertNull(activitiProcess);
    }

    @Test
    public void testGetActivitiProcessWithNonExistentId() {
        ActivitiProcess activitiProcess = activitiService.getActivitiProcess(spaceId, TestUtils.generateRandomString());

        assertNull(activitiProcess);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStartActivitProcess() throws Exception {
        String processInstanceId = TestUtils.generateRandomString();
        ProcessInstance processInstance = prepareActiveProcessInstance(idOfCurrentService, implementationId, processInstanceId);
        when(activitiFacade.startProcessInstance(eq(userId), eq(implementationId), any(Map.class))).thenReturn(processInstance);

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(SlpObjectFactory.createParameter(PARAM_SID, "ABC"));

        com.sap.lmsl.slp.Process process = activitiService.startProcess(spaceId, userId, parameters);
        assertEquals(processInstanceId, process.getId());
        assertEquals(serviceMetadata.getDisplayName(), process.getDisplayName());
        assertEquals(serviceMetadata.getDescription(), process.getDescription());
        assertEquals(serviceMetadata.getId(), process.getService());

        @SuppressWarnings("rawtypes")
        ArgumentCaptor<Map> processStartParametersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(activitiFacade).startProcessInstance(eq(userId), eq(implementationId), processStartParametersCaptor.capture());
        Map<String, Object> processStartParameters1 = processStartParametersCaptor.getValue();
        assertEquals("ABC", processStartParameters1.get(PARAM_SID));
    }

    @Test
    public void testGetServiceMetadata() {
        assertEquals(serviceMetadata, activitiService.getServiceMetadata());
    }

    @Test
    public void testGetService() {
        Service service = activitiService.getService();
        assertNotNull(service.getId());
        assertEquals(serviceMetadata.getId(), service.getId());

        assertNotNull(service.getDisplayName());
        assertEquals(serviceMetadata.getDisplayName(), service.getDisplayName());

        assertNotNull(service.getDescription());
        assertEquals(serviceMetadata.getDescription(), service.getDescription());

        assertEquals(parametersMetadata.size(), service.getParameters().getParameter().size());
        for (ParameterMetadata parameterMetadata : parametersMetadata) {
            assertTrue(exists(service.getParameters().getParameter(), parameterMetadata));
        }
    }

    private boolean exists(List<Parameter> parameters, ParameterMetadata metadata) {
        for (Parameter parameter : parameters) {
            if (parameter.getId().equals(metadata.getId()) && parameter.isRequired().equals(metadata.isRequired())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testGetActiveProcess1() {
        String processInstanceId = TestUtils.generateRandomString();

        ProcessInstance processInstance = prepareActiveProcessInstance(idOfCurrentService, implementationId, processInstanceId);
        when(activitiFacade.getActiveProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(processInstance);

        Process process = activitiService.getProcess(spaceId, processInstanceId);
        assertEquals(processInstanceId, process.getId());
    }

    @Test
    public void testGetActiveProcess2() {
        String processInstanceId = TestUtils.generateRandomString();

        ProcessInstance processInstance = prepareActiveProcessInstance(idOfAnotherService, implementationId, processInstanceId);
        when(activitiFacade.getActiveProcessInstance(implementationId, spaceId, processInstanceId)).thenReturn(processInstance);

        assertNull(activitiService.getProcess(spaceId, processInstanceId));
    }

    @Test
    public void testGetProcesses() {
        String processInstanceId1 = TestUtils.generateRandomString();
        String processInstanceId2 = TestUtils.generateRandomString();
        String processInstanceId3 = TestUtils.generateRandomString();
        String processInstanceId4 = TestUtils.generateRandomString();

        ProcessInstance processInstance1 = prepareActiveProcessInstance(idOfCurrentService, implementationId, processInstanceId1);
        ProcessInstance processInstance2 = prepareActiveProcessInstance(idOfAnotherService, implementationId, processInstanceId2);
        when(activitiFacade.getActiveProcessInstances(implementationId, spaceId)).thenReturn(
            Arrays.asList(processInstance1, processInstance2));

        HistoricProcessInstance processInstance3 = prepareHistoricProcessInstance(idOfCurrentService, implementationId, processInstanceId3);
        HistoricProcessInstance processInstance4 = prepareHistoricProcessInstance(idOfAnotherService, implementationId, processInstanceId4);
        when(activitiFacade.getHistoricProcessInstances(implementationId, spaceId)).thenReturn(
            Arrays.asList(processInstance3, processInstance4));

        List<Process> processList = activitiService.getProcesses(spaceId).getProcess();
        assertEquals(2, processList.size());
    }

    private ProcessInstance prepareActiveProcessInstance(String serviceId, String processDefinitionId, String processInstanceId) {
        ProcessInstance processInstance = getActiveProcessInstanceMock(serviceId, processDefinitionId, processInstanceId);
        mockGetServiceIdOfProcessInstance(processInstanceId, serviceId);
        mockCreateActivitiProcessInstance(processInstance);
        return processInstance;
    }

    private ProcessInstance getActiveProcessInstanceMock(String serviceId, String processDefinitionId, String processInstanceId) {
        ProcessInstance mock = mock(ProcessInstance.class);
        stub(mock.getProcessDefinitionId()).toReturn(processDefinitionId);
        stub(mock.getId()).toReturn(processInstanceId);
        return mock;
    }

    private void mockCreateActivitiProcessInstance(ProcessInstance processInstance) {
        ActiveActivitiProcess activeActivitiProcess = new ActiveActivitiProcess(serviceMetadata, processInstance) {
            @Override
            protected TaskExtensionService getTaskExtensionService() {
                TaskExtensionService mockService = mock(TaskExtensionService.class);
                when(mockService.findByProcessId(Mockito.anyString())).thenReturn(Collections.<ProgressMessage> emptyList());
                return mockService;
            }

            @Override
            public Parameters getParameters() {
                return new Parameters();
            };
        };
        when(activitiProcessFactory.create(serviceMetadata, processInstance)).thenReturn(activeActivitiProcess);

    }

    private void mockGetServiceIdOfProcessInstance(String processInstanceId, String serviceId) {
        when(activitiFacade.getServiceId(processInstanceId)).thenReturn(serviceId);
    }

    private HistoricProcessInstance prepareHistoricProcessInstance(String serviceId, String processDefinitionId, String processInstanceId) {
        HistoricProcessInstance processInstance = getHistoricProcessInstanceMock(serviceId, processDefinitionId, processInstanceId);
        mockCreateActivitiProcessInstance(processInstance);
        mockGetServiceIdOfProcessInstance(processInstanceId, serviceId);
        return processInstance;
    }

    private HistoricProcessInstance getHistoricProcessInstanceMock(String serviceId, String processDefinitionId, String processInstanceId) {
        HistoricProcessInstance processInstance = Mockito.mock(HistoricProcessInstance.class);
        stub(processInstance.getId()).toReturn(processInstanceId);
        stub(processInstance.getProcessDefinitionId()).toReturn(processDefinitionId);
        return processInstance;
    }

    private void mockCreateActivitiProcessInstance(HistoricProcessInstance historicInstance) {
        FinishedActivitiProcess finishedActivitiProcess = new FinishedActivitiProcess(serviceMetadata, historicInstance) {
            @Override
            protected TaskExtensionService getTaskExtensionService() {
                TaskExtensionService mockService = mock(TaskExtensionService.class);
                when(mockService.findByProcessId(Mockito.anyString())).thenReturn(Collections.<ProgressMessage> emptyList());
                return mockService;
            }

            @Override
            public Parameters getParameters() {
                return new Parameters();
            };
        };
        when(activitiProcessFactory.create(serviceMetadata, historicInstance)).thenReturn(finishedActivitiProcess);
    }

}
