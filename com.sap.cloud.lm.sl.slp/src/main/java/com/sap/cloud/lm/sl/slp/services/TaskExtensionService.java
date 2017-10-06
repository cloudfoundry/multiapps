package com.sap.cloud.lm.sl.slp.services;

import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;

public class TaskExtensionService extends ProgressMessageService {

    private static final String TABLE_NAME = "TASK_EXTENSION";

    private static TaskExtensionService instance;

    TaskExtensionService() {
        super(TABLE_NAME);
    }

    public static TaskExtensionService getInstance() {
        if (instance == null) {
            instance = new TaskExtensionService();
        }
        return instance;
    }

}
