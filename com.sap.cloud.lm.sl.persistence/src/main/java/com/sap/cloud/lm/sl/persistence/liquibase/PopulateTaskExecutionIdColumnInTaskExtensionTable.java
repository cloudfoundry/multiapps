package com.sap.cloud.lm.sl.persistence.liquibase;

public class PopulateTaskExecutionIdColumnInTaskExtensionTable extends PopulateTaskExecutionIdColumnInProgressMessageTable {

    private static final String TABLE_NAME = "TASK_EXTENSION";

    public PopulateTaskExecutionIdColumnInTaskExtensionTable() {
        super(TABLE_NAME);
    }

}
