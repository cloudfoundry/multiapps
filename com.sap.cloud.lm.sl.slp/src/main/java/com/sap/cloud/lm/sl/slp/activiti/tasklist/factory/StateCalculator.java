package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

import com.sap.lmsl.slp.SlpTaskState;

class StateCalculator {

    private int tasksInActionRequired = 0;
    private int runningTasks = 0;
    private int finishedTasks = 0;
    private int tasksInDialog = 0;
    private int tasksInError = 0;
    private int tasksInInitial = 0;
    private int tasksInSkipped = 0;
    private int tasksInAbortedState = 0;

    public SlpTaskState getState() {
        if (tasksInAbortedState > 0) {
            return SlpTaskState.SLP_TASK_STATE_ABORTED;
        }
        if (tasksInActionRequired > 0) {
            return SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED;
        }
        if (tasksInDialog > 0) {
            return SlpTaskState.SLP_TASK_STATE_DIALOG;
        }
        if (runningTasks > 0) {
            if (tasksInError > 0) {
                return SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED;
            }
            return SlpTaskState.SLP_TASK_STATE_RUNNING;
        }
        if (tasksInError > 0) {
            return SlpTaskState.SLP_TASK_STATE_ERROR;
        }
        if (tasksInInitial > 0) {
            return SlpTaskState.SLP_TASK_STATE_INITIAL;
        }
        if (finishedTasks > 0) {
            return SlpTaskState.SLP_TASK_STATE_FINISHED;
        }
        if (tasksInSkipped > 0) {
            return SlpTaskState.SLP_TASK_STATE_SKIPPED;
        }
        return SlpTaskState.SLP_TASK_STATE_INITIAL;
    }

    public void updateState(SlpTaskState state) {
        switch (state) {
            case SLP_TASK_STATE_ACTION_REQUIRED:
                tasksInActionRequired++;
                break;
            case SLP_TASK_STATE_RUNNING:
                runningTasks++;
                break;
            case SLP_TASK_STATE_FINISHED:
                finishedTasks++;
                break;
            case SLP_TASK_STATE_INITIAL:
                tasksInInitial++;
                break;
            case SLP_TASK_STATE_DIALOG:
                tasksInDialog++;
                break;
            case SLP_TASK_STATE_SKIPPED:
                tasksInSkipped++;
                break;
            case SLP_TASK_STATE_ERROR:
                tasksInError++;
                break;
            case SLP_TASK_STATE_ABORTED:
                tasksInAbortedState++;
                break;
            default:
                break;
        }
    }
}
