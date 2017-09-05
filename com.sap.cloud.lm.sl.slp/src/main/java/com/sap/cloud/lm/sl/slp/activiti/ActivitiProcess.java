package com.sap.cloud.lm.sl.slp.activiti;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.activiti.engine.history.HistoricActivityInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.services.FileStorageException;
import com.sap.cloud.lm.sl.persistence.services.ProgressMessageService;
import com.sap.cloud.lm.sl.slp.activiti.ProcessError.ErrorType;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.ProcessState;
import com.sap.cloud.lm.sl.slp.activiti.tasklist.StepBuilder;
import com.sap.cloud.lm.sl.slp.ext.TaskExtensionUtil;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;
import com.sap.cloud.lm.sl.slp.services.ProcessLogsPersistenceService;
import com.sap.cloud.lm.sl.slp.services.TaskExtensionService;
import com.sap.lmsl.slp.Action;
import com.sap.lmsl.slp.Actions;
import com.sap.lmsl.slp.Config;
import com.sap.lmsl.slp.Error;
import com.sap.lmsl.slp.Log;
import com.sap.lmsl.slp.Logs;
import com.sap.lmsl.slp.Monitor;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Processes;
import com.sap.lmsl.slp.SlpActionEnum;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.Task;
import com.sap.lmsl.slp.Tasklist;

public abstract class ActivitiProcess {

    public static final String ACTION_ID_RETRY = "retry";
    public static final String ACTION_ID_ABORT = "abort";
    public static final String ACTION_ID_RESUME = "resume";
    public static final String ACTION_ID_START = "start";

    public static final String ACTIVITY_TASK_TYPE_SERVICE = "serviceTask";
    public static final String ACTIVITY_TASK_TYPE_RECEIVE = "receiveTask";

    private static SlpTaskState[] MONITOR_STATES = { SlpTaskState.SLP_TASK_STATE_ACTION_REQUIRED, SlpTaskState.SLP_TASK_STATE_BREAKPOINT,
        SlpTaskState.SLP_TASK_STATE_DIALOG, SlpTaskState.SLP_TASK_STATE_ERROR, SlpTaskState.SLP_TASK_STATE_RUNNING,
        SlpTaskState.SLP_TASK_STATE_STOPPED };

    private static final Map<String, Action> ACTIONS = new HashMap<String, Action>();

    static {
        ACTIONS.put(ACTION_ID_RESUME, SlpObjectFactory.createAction(ACTION_ID_RESUME, SlpActionEnum.SLP_ACTION_RESUME));
        ACTIONS.put(ACTION_ID_ABORT, SlpObjectFactory.createAction(ACTION_ID_ABORT, SlpActionEnum.SLP_ACTION_ABORT));
        ACTIONS.put(ACTION_ID_RETRY, SlpObjectFactory.createAction(ACTION_ID_RETRY, SlpActionEnum.SLP_ACTION_REPEAT));
        ACTIONS.put(ACTION_ID_START, SlpObjectFactory.createAction(ACTION_ID_START, SlpActionEnum.SLP_ACTION_START));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcess.class);

    @Inject
    ActivitiFacade activitiFacade;

    @Inject
    private ProgressMessageService progressMessageService;

    @Inject
    private ProcessLogsPersistenceService processLogsPersistenceService;

    @Inject
    private TaskExtensionService taskExtensionService;

    protected final ServiceMetadata serviceMetadata;
    private VariableHandler vh;
    private ProcessError error;
    private List<ProgressMessage> progressMessages;
    private List<HistoricActivityInstance> historicActivities;
    private List<ProgressMessage> taskExtensions;

    protected ActivitiProcess(ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    public ServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }

    public static Processes getProcesses(List<ActivitiProcess> activitiProcesses) {
        List<Process> processes = new ArrayList<Process>();
        for (ActivitiProcess activitiProcess : activitiProcesses) {
            processes.add(activitiProcess.getProcess());
        }
        return SlpObjectFactory.createProcesses(processes);
    }

    public Process getProcess() {
        return SlpObjectFactory.createProcess(getProcessInstanceId(), serviceMetadata.getDisplayName(), serviceMetadata.getDescription(),
            serviceMetadata.getId(), getStatus(), getParameters(), TaskExtensionUtil.getTaskExtensions(getTaskExtensions()));
    }

    public Tasklist getTasklist() {
        return ActivitiTask.getTasklist(getActivitiTasks());
    }

    public Monitor getMonitor() {
        List<ActivitiTask> tasks = getMonitorTasks();
        List<Task> taskList = ActivitiTask.getTasklist(tasks).getTask();
        return SlpObjectFactory.createMonitor(taskList);
    }

    private List<ActivitiTask> getMonitorTasks() {
        List<ActivitiTask> tasks = getActivitiTasks();
        if (getStatus() == SlpProcessState.SLP_PROCESS_STATE_ACTIVE) {
            tasks = filterActivitiTasks(tasks, MONITOR_STATES);
        }
        return tasks;
    }

    private List<ActivitiTask> filterActivitiTasks(List<ActivitiTask> tasks, SlpTaskState... allowedStates) {
        List<ActivitiTask> result = new ArrayList<ActivitiTask>();
        for (ActivitiTask task : tasks) {
            for (SlpTaskState allowedState : allowedStates) {
                if (task.getStatus().equals(allowedState)) {
                    result.add(task);
                    break;
                }
            }
        }
        return result;
    }

    public Actions getActions() {
        return SlpObjectFactory.createActions(getActionList(getActionIds()));
    }

    public Error getError() {
        ProcessError processError = getProcessError();
        Error error = null;

        if (processError != null) {
            error = SlpObjectFactory.createError(processError.getCode(), processError.getMessage());
        }
        return error;
    }

    public Parameters getParameters() {
        return SlpObjectFactory.createParameters(getParametersList());
    }

    protected List<Parameter> getParametersList() {
        if (!includeParameters()) {
            return Collections.emptyList();
        }
        return ActivitiParameter.getParameters(serviceMetadata.getParameters(), getVariableHandler());
    }

    private List<ActivitiTask> getActivitiTasks() {
        // all input for tasklist buiding except for loop variables that are taken from the variable
        // handler
        ProcessState processState = fetchProcessState();

        // LOGGER.info(MessageFormat.format("Process state JSON {0}", JsonUtil.toJson(processState, true)));

        Step step = new StepBuilder(processState, getVariableHandler()).buildStep(serviceMetadata);
        List<ActivitiTask> activitiTasks = step.toActivitiTasks();

        return activitiTasks;
    }

    private ProcessState fetchProcessState() {
        List<HistoricActivityInstance> historicInstances = getHistoricActivities();
        List<ProgressMessage> progressMessages = getProgressMessages();
        List<ProgressMessage> taskExtensions = getTaskExtensions();
        SlpTaskState currentTaskState = getCurrentState();

        String currentTaskId = findCurrentTaskId(historicInstances, progressMessages);

        return new ProcessState(historicInstances, progressMessages, taskExtensions, currentTaskState, currentTaskId,
            serviceMetadata.getStepIds());
    }

    private String findCurrentTaskId(List<HistoricActivityInstance> historicInstances, List<ProgressMessage> progressMessages) {
        Date currentTaskStartTime = null;
        String currentTaskId = null;
        for (HistoricActivityInstance historicInstance : historicInstances) {
            if (currentTaskStartTime == null || currentTaskStartTime.before(historicInstance.getStartTime())) {
                currentTaskStartTime = historicInstance.getStartTime();
                currentTaskId = historicInstance.getId();
            }
        }

        LOGGER.debug("Last known execution time from historic activities: " + currentTaskStartTime);
        for (ProgressMessage progressMessage : progressMessages) {
            if (currentTaskStartTime == null || currentTaskStartTime.before(progressMessage.getTimestamp())) {
                currentTaskStartTime = progressMessage.getTimestamp();
                currentTaskId = progressMessage.getTaskId();
            }
        }
        LOGGER.debug("Last known execution time from progress messagses: " + currentTaskStartTime);
        return currentTaskId;
    }

    protected List<HistoricActivityInstance> getHistoricActivities() {
        if (historicActivities == null) {
            historicActivities = loadHistoricActivities();
        }
        return historicActivities;
    }

    protected List<HistoricActivityInstance> loadHistoricActivities() {
        List<String> relevantIds = serviceMetadata.getStepIds();
        List<String> subProcessIds = getActivitiFacade().getHistoricSubProcessIds(getProcessInstanceId());
        List<HistoricActivityInstance> receiveTaskActivities = computeReceiveTasks(subProcessIds);
        List<HistoricActivityInstance> startEventActivities = getHistoricActivityInstances("startEvent", getProcessInstanceId());
        List<HistoricActivityInstance> endEventActivities = getHistoricActivityInstances("endEvent", getProcessInstanceId());
        List<HistoricActivityInstance> all = new ArrayList<>();
        all.addAll(receiveTaskActivities);
        all.addAll(startEventActivities);
        all.addAll(endEventActivities);
        // filter only ones that are described in the metadata
        List<HistoricActivityInstance> relevantActivities = new LinkedList<HistoricActivityInstance>();
        for (HistoricActivityInstance activiti : all) {
            if (relevantIds.contains(activiti.getActivityId()))
                relevantActivities.add(activiti);
        }
        return relevantActivities;
    }

    private List<HistoricActivityInstance> computeReceiveTasks(List<String> subProcessIds) {
        List<HistoricActivityInstance> result = new ArrayList<>();
        List<HistoricActivityInstance> superProcessReceiveTasks = getHistoricActivityInstances("receiveTask", getProcessInstanceId());
        for (String subProcessId : subProcessIds) {
            List<HistoricActivityInstance> subProcessReceiveTasks = getHistoricActivityInstances("receiveTask", subProcessId);
            LOGGER.debug(MessageFormat.format("Historic activities for process with id {0}: {1}", subProcessId,
                JsonUtil.toJson(subProcessReceiveTasks)));
            result.addAll(subProcessReceiveTasks);
        }
        result.addAll(superProcessReceiveTasks);
        return result;
    }

    private List<HistoricActivityInstance> getHistoricActivityInstances(String type, String processInstanceId) {
        return getActivitiFacade().getHistoricActivities(type, processInstanceId);
    }

    private List<ProgressMessage> getProgressMessages() {
        if (progressMessages == null) {
            progressMessages = getProgressMessages(getProgressMessageService());
        }
        return progressMessages;
    }

    private List<ProgressMessage> getTaskExtensions() {
        if (taskExtensions == null) {
            taskExtensions = getProgressMessages(getTaskExtensionService());
        }
        return taskExtensions;
    }

    private List<ProgressMessage> getProgressMessages(ProgressMessageService progressMessageService) {
        try {
            List<ProgressMessage> allProgressMessagesForProcess = progressMessageService.findByProcessId(getProcessInstanceId());
            Pair<List<ProgressMessage>, List<ProgressMessage>> relevantAndNonRelevantProgressMessages = filterProgressMessages(
                allProgressMessagesForProcess, serviceMetadata.getStepIds());
            if (!relevantAndNonRelevantProgressMessages._2.isEmpty()) {
                LOGGER.warn(
                    MessageFormat.format("Non-task related progress messsages exists {0}", relevantAndNonRelevantProgressMessages._2));
            }
            return relevantAndNonRelevantProgressMessages._1;
        } catch (SLException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<List<ProgressMessage>, List<ProgressMessage>> filterProgressMessages(List<ProgressMessage> allProgressMessagesForProcess,
        List<String> stepIds) {
        List<ProgressMessage> relevantProgressMessage = new ArrayList<>();
        List<ProgressMessage> nonRelevantProgressMessage = new ArrayList<>();
        for (ProgressMessage progressMessage : allProgressMessagesForProcess) {
            if (contains(stepIds, progressMessage)) {
                relevantProgressMessage.add(progressMessage);
            } else {
                nonRelevantProgressMessage.add(progressMessage);
            }
        }
        return new Pair<List<ProgressMessage>, List<ProgressMessage>>(relevantProgressMessage, nonRelevantProgressMessage);
    }

    private boolean contains(List<String> stepIds, ProgressMessage progressMessage) {
        for (String stepId : stepIds) {
            if (progressMessage.getTaskId().startsWith(stepId)) {
                return true;
            }
        }
        return false;
    }

    public abstract SlpProcessState getStatus();

    public abstract SlpTaskState getCurrentState();

    public abstract List<String> getActionIds();

    public abstract void executeAction(String userId, String actionId);

    public abstract void delete(String userId);

    protected abstract String getProcessInstanceId();

    public abstract String getCurrentActivityId();

    protected abstract boolean includeParameters();

    public abstract Date getProcessStartTime();

    public ProcessError getProcessError() {
        if (error == null) {
            error = calculateError();
        }
        return error;
    }

    private ProcessError calculateError() {
        List<ProgressMessage> messages = getErrorMessages(getProgressMessages());
        String errorMessage = getTextFromMessages(messages);
        return (!errorMessage.isEmpty()) ? new ProcessError(ErrorType.PROCESS_EXECUTION_ERROR, errorMessage) : null;
    }

    private List<ProgressMessage> getErrorMessages(List<ProgressMessage> progressMessages) {
        List<ProgressMessage> result = new LinkedList<>();
        for (ProgressMessage progressMessage : progressMessages) {
            if (ProgressMessageType.ERROR.equals(progressMessage.getType())) {
                result.add(progressMessage);
            }
        }
        return result;
    }

    private String getTextFromMessages(List<ProgressMessage> messages) {
        List<String> text = new ArrayList<String>();
        for (ProgressMessage message : messages) {
            text.add(message.getText());
        }
        String joinedText = StringUtils.join(text.toArray(new String[messages.size()]), "\n");
        return joinedText;
    }

    protected VariableHandler getVariableHandler() {
        if (vh == null) {
            vh = new ActivitiVariableHandler(getProcessInstanceId(), getActivitiFacade());
        }
        return vh;
    }

    private static List<Action> getActionList(List<String> actionIds) {
        List<Action> actions = new ArrayList<Action>();
        for (String actionId : actionIds) {
            actions.add(ACTIONS.get(actionId));
        }
        return actions;
    }

    public Action getAction(String actionId) {
        for (Action action : getActions().getAction()) {
            if (action.getId().equals(actionId)) {
                return action;
            }
        }
        return null;
    }

    public Task getTaskById(String taskId) {
        return getActivitiTaskById(getActivitiTasks(), taskId).getTask();
    }

    public Task getMonitorTaskById(String taskId) {
        return getActivitiTaskById(getMonitorTasks(), taskId).getTask();
    }

    private ActivitiTask getActivitiTaskById(List<ActivitiTask> allTasks, String taskId) {
        for (ActivitiTask activitiTask : allTasks) {
            if (activitiTask.getIndexedId().equals(taskId))
                return activitiTask;
        }
        return null;
    }

    public Error getErrorByTaskId(String taskId) {
        ProcessError error = getActivitiTaskById(getActivitiTasks(), taskId).getError();
        return SlpObjectFactory.createError(error.getCode(), error.getMessage());
    }

    public Error getMonitorTaskErrorById(String taskId) {
        ProcessError error = getActivitiTaskById(getMonitorTasks(), taskId).getError();
        return SlpObjectFactory.createError(error.getCode(), error.getMessage());
    }

    public Config getConfig() {
        return SlpObjectFactory.createConfig(getParametersList());
    }

    public Parameter getConfigParameter(String parameterId) {
        for (Parameter parameter : getParametersList()) {
            if (parameter.getId().equals(parameterId))
                return parameter;
        }
        return null;
    }

    public Config setConfig(Config config) throws SLException {
        Map<String, Object> activitiVariables = ActivitiParameter.getActivitiVariables(config.getParameter(),
            serviceMetadata.getParameters(), false, false);
        setVariables(activitiVariables);
        return getConfig();
    }

    protected abstract void setVariables(Map<String, Object> variablesToSet);

    public Logs getLogs(String space) throws FileStorageException {
        return SlpObjectFactory.createLogs(getProcessLogs(space).toArray(new ProcessLog[] {}));
    }

    private List<ProcessLog> getProcessLogs(String space) throws FileStorageException {
        List<ProcessLog> processLogs = new ArrayList<ProcessLog>();

        List<String> logVariables = getProcessLogsPersistence().getLogNames(space, getProcessInstanceId());
        for (String logVariable : logVariables) {
            ProcessLog processLog = createProcessLog(logVariable);
            processLogs.add(processLog);
        }

        return processLogs;
    }

    private ProcessLog createProcessLog(String id) {
        ProcessLog processLog = new ProcessLog();
        processLog.setId(id);
        return processLog;
    }

    public Log getLog(String space, String logId) throws FileStorageException {
        for (ProcessLog processLog : getProcessLogs(space)) {
            if (processLog.getId().equals(logId))
                return SlpObjectFactory.createLog(processLog);
        }
        return null;
    }

    public String getLogContent(String space, String logId) throws FileStorageException {
        return getProcessLogsPersistence().getLogContent(space, getProcessInstanceId(), logId);
    }

    private ProcessLogsPersistenceService getProcessLogsPersistence() {
        if (processLogsPersistenceService == null) {
            processLogsPersistenceService = ProcessLogsPersistenceService.getInstance();
        }
        return processLogsPersistenceService;
    }

    protected ProgressMessageService getProgressMessageService() {
        if (progressMessageService == null) {
            progressMessageService = ProgressMessageService.getInstance();
        }
        return progressMessageService;
    }

    protected TaskExtensionService getTaskExtensionService() {
        if (taskExtensionService == null) {
            taskExtensionService = TaskExtensionService.getInstance();
        }
        return taskExtensionService;
    }

    protected ActivitiFacade getActivitiFacade() {
        if (activitiFacade == null) {
            activitiFacade = ActivitiFacade.getInstance();
        }
        return activitiFacade;
    }
}
