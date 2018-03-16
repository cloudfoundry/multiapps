package com.sap.activiti.common.util;

public interface ISkipHelper {

    public static enum SkipRequest {

        SKIP, NONE;

    };

    boolean hasSkipRequest(String logicalStepName);

    void createSkipRequest(String logicalStepName);

    void removeSkipRequest(String logicalStepName);

}
