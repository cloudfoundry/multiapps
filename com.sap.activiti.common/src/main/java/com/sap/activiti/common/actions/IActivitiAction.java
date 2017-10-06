package com.sap.activiti.common.actions;

public interface IActivitiAction {

    public enum ActionType {
        SKIP, RETRY, ABORT, UPDATE_VARIABLES, WRITE_MEMO
    };

    public Enum<?> getType();

    public void execute();
}