package com.sap.activiti.common;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

public class MockProcessEngine {

    public static ProcessEngine createMockInstance() {
        ProcessEngine engine = mock(ProcessEngine.class);

        doReturn(mock(TaskService.class)).when(engine)
            .getTaskService();
        doReturn(mock(RuntimeService.class)).when(engine)
            .getRuntimeService();
        doReturn(mock(FormService.class)).when(engine)
            .getFormService();
        doReturn(mock(HistoryService.class)).when(engine)
            .getHistoryService();
        doReturn(mock(IdentityService.class)).when(engine)
            .getIdentityService();
        doReturn(mock(ManagementService.class)).when(engine)
            .getManagementService();
        doReturn(mock(RepositoryService.class)).when(engine)
            .getRepositoryService();

        return engine;
    }

}
