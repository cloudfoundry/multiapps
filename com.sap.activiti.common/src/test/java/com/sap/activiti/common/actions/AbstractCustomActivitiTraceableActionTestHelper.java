package com.sap.activiti.common.actions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.UserQueryImpl;

import com.sap.activiti.common.ActivitiQuerySpyBuilder;
import com.sap.activiti.common.MockProcessEngine;

public class AbstractCustomActivitiTraceableActionTestHelper {

    public User createUser(String firstName, String lastName) {
        User user = mock(User.class);

        when(user.getFirstName()).thenReturn(firstName);
        when(user.getLastName()).thenReturn(lastName);

        return user;
    }

    public ProcessEngine createProcessEngine(User user) {
        ProcessEngine engine = MockProcessEngine.createMockInstance();

        mockIdentitytService(engine, user);

        return engine;
    }

    private void mockIdentitytService(ProcessEngine engine, User user) {
        UserQuery query = ActivitiQuerySpyBuilder.<UserQuery, User> createBuilder(new UserQueryImpl()) //
            .setSingleResult(user)
            .getQuery();

        IdentityService identityService = engine.getIdentityService();
        when(identityService.createUserQuery()).thenReturn(query);
    }
}
