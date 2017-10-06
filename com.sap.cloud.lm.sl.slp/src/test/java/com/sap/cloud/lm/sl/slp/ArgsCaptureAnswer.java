package com.sap.cloud.lm.sl.slp;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

//mock a runtime service that captures process id on deletion
public class ArgsCaptureAnswer implements Answer<Void> {

    private Object[] args;

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        args = invocation.getArguments();
        return null;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getValue() {
        return (String) args[0];
    }
}
