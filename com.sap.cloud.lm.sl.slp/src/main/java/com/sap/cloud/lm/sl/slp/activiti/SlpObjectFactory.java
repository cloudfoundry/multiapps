package com.sap.cloud.lm.sl.slp.activiti;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.slp.Constants;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.lmsl.slp.Action;
import com.sap.lmsl.slp.Actions;
import com.sap.lmsl.slp.ComponentVersion;
import com.sap.lmsl.slp.Config;
import com.sap.lmsl.slp.Error;
import com.sap.lmsl.slp.File;
import com.sap.lmsl.slp.Files;
import com.sap.lmsl.slp.Log;
import com.sap.lmsl.slp.Logs;
import com.sap.lmsl.slp.Metadata;
import com.sap.lmsl.slp.Monitor;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Processes;
import com.sap.lmsl.slp.ProgressMessage;
import com.sap.lmsl.slp.Service;
import com.sap.lmsl.slp.Services;
import com.sap.lmsl.slp.SlpActionEnum;
import com.sap.lmsl.slp.SlpParameterType;
import com.sap.lmsl.slp.SlpProcessState;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.SlpTaskType;
import com.sap.lmsl.slp.Task;
import com.sap.lmsl.slp.Task.ProgressMessages;
import com.sap.lmsl.slp.Tasklist;
import com.sap.lmsl.slp.Versions;

/**
 * Utility class for creating SLP entities
 */
public class SlpObjectFactory {

    static final String SLMP_VERSION = "1.2.0";
    static final String SLPP_VERSION = "1.2.0";

    private static final String SERVICE_PROCESSES = "services/%s/processes";
    private static final String SERVICE_VERSIONS = "services/%s/versions";
    private static final String SERVICE_FILES = "services/%s/files";
    private static final String PROCESS_ROOT_URL = "runs/%s/%s";
    private static final String ERROR_URI = "error";
    private static final String LOGCONTENT_URI = "logs/%s/content";
    private static final Integer DEFAULT_RERESH_RATE = 10;

    public static Metadata createSlmpMetadata() {
        Metadata metadata = new Metadata();
        metadata.setSlmpversion(SLMP_VERSION);
        return metadata;
    }

    public static Metadata createSlppMetadata() {
        Metadata metadata = new Metadata();
        metadata.setSlppversion(SLPP_VERSION);
        return metadata;
    }

    static Services createServices(List<Service> services) {
        Services servicesx = new Services();
        servicesx.getService().addAll(services);
        return servicesx;
    }

    static Service createService(String id, String displayName, String description, List<Parameter> parameters) {
        Service service = new Service();
        service.setId(id);
        service.setDisplayName(displayName);
        service.setDescription(description);
        service.setParameters(createParameters(parameters));
        service.setProcesses(String.format(SERVICE_PROCESSES, id));
        service.setVersions(String.format(SERVICE_VERSIONS, id));
        service.setFiles(String.format(SERVICE_FILES, id));
        service.setSlppversion(SLPP_VERSION);
        return service;
    }

    public static Versions createVersions() {
        return new Versions();
    }

    public static ComponentVersion createComponentVersion(String id, String serviceId, String version) {
        ComponentVersion componentVersion = new ComponentVersion();
        componentVersion.setId(id);
        componentVersion.setComponent(serviceId);
        componentVersion.setVersion(version);
        return componentVersion;
    }

    static Parameters createParameters(List<Parameter> parameters) {
        Parameters parametersx = new Parameters();
        parametersx.getParameter().addAll(parameters);
        return parametersx;
    }

    static Parameter createParameter(String id, SlpParameterType type, boolean required, boolean secure) {
        Parameter parameter = new Parameter();
        parameter.setId(id);
        parameter.setType(type);
        parameter.setRequired(required);
        parameter.setSecure(secure);
        return parameter;
    }

    public static Parameter createParameter(String id, Parameter.Tablevalue tablevalue, boolean required, boolean secure) {
        Parameter parameter = createParameter(id, SlpParameterType.SLP_PARAMETER_TYPE_TABLE, required, secure);
        parameter.setTablevalue(tablevalue);
        return parameter;
    }

    static Parameter createParameter(String id, Object value, Object defaultValue, boolean required, boolean secure) {
        Parameter parameter = createParameter(id, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR, required, secure);
        if (value != null) {
            parameter.setValue(value.toString());
        }
        if (defaultValue != null) {
            parameter.setDefault(defaultValue.toString());
        }
        return parameter;
    }

    static Parameter createParameter(String id, Object value) {
        Parameter parameter = new Parameter();
        parameter.setId(id);
        if (value != null) {
            parameter.setValue(value.toString());
        }
        return parameter;
    }

    static Processes createProcesses(List<Process> processes) {
        Processes processesx = new Processes();
        processesx.getProcess().addAll(processes);
        return processesx;
    }

    static Process createProcess(String id, String displayName, String description, String serviceId, SlpProcessState status,
        Parameters parameters, Map<String, String> extensionElements) {
        Process process = new Process();
        process.setId(id);
        process.setDisplayName(displayName);
        process.setDescription(description);
        process.setService(serviceId);
        process.setStatus(status);
        process.setRootURL(String.format(PROCESS_ROOT_URL, serviceId, id));
        process.setParameters(parameters);

        if (extensionElements != null && !extensionElements.isEmpty()) {
            process.getAny().addAll(getExtensionElements(extensionElements));
        }

        return process;
    }

    static Tasklist createTasklist(List<Task> tasks) {
        Tasklist tasklist = new Tasklist();
        tasklist.getTask().addAll(tasks);
        return tasklist;
    }

    static Task createTask(String id, String displayName, String description, String parentId, SlpTaskType type, SlpTaskState status,
        int progress, Date startTime, Date endTime, ProcessError error, List<String> progressMessages,
        Map<String, String> extensionElements) {
        Task task = new Task();
        task.setId(id);
        task.setDisplayName(displayName);
        task.setDescription(description);
        if (parentId != null) {
            task.setParent(parentId);
        }
        task.setType(type);
        task.setStatus(status);
        task.setProgress(progress);
        if (startTime != null) {
            task.setStartedAt(dateAsString(startTime));
        }
        if (endTime != null) {
            task.setFinishedAt(dateAsString(endTime));
        }
        if (error != null) {
            String errorLink = getErrorURI();
            task.setError(errorLink);
        }

        if (progressMessages != null) {
            task.setProgressMessages(createProgressMessages(progressMessages));
        }
        if (extensionElements != null && !extensionElements.isEmpty()) {
            task.getAny().addAll(getExtensionElements(extensionElements));
        }
        task.setRefreshRate(DEFAULT_RERESH_RATE);

        return task;
    }

    static String getErrorURI() {
        return ERROR_URI;
    }

    private static Collection<? extends Element> getExtensionElements(Map<String, String> extensionElements) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(Messages.FAILED_TO_ADD_TASK_EXTENSION_ELEMENTS, e);
        }
        Document doc = builder.newDocument();

        List<Element> result = new ArrayList<Element>();
        for (Map.Entry<String, String> entry : extensionElements.entrySet()) {
            Element element = doc.createElement(entry.getKey());
            Text text = doc.createTextNode(entry.getValue());
            element.appendChild(text);

            result.add(element);
        }

        return result;
    }

    private static ProgressMessages createProgressMessages(List<String> messages) {
        ProgressMessages progressMessages = new ProgressMessages();
        for (int i = 0; i < messages.size(); ++i) {
            String message = messages.get(i);
            ProgressMessage progressMessage = new ProgressMessage();
            progressMessage.setId(i);
            progressMessage.setMessage(message);
            progressMessages.getProgressMessage().add(progressMessage);
        }
        return progressMessages;
    }

    static Actions createActions(List<Action> actions) {
        Actions actionsx = new Actions();
        actionsx.getAction().addAll(actions);
        return actionsx;
    }

    static Action createAction(String id, SlpActionEnum type) {
        Action action = new Action();
        action.setId(id);
        action.setActionType(type.value());
        return action;
    }

    static Error createError(String errorCode, String errorMessage) {
        Error error = new Error();
        error.setCode(errorCode);
        error.setDescription(errorMessage);
        return error;
    }

    public static Files createFiles(FileEntry... fileDescriptions) {
        Files files = new Files();
        for (FileEntry fileDesc : fileDescriptions) {
            File file = new File();
            file.setId(fileDesc.getId());
            file.setFileName(fileDesc.getName());
            file.setFilePath(fileDesc.getNamespace());
            file.setFileSize(fileDesc.getSize());
            file.setDigest(fileDesc.getDigest());
            file.setDigestAlgorithm(fileDesc.getDigestAlgorithm());
            file.setModificationTime(dateAsString(fileDesc.getModified()));
            files.getFile().add(file);
        }
        return files;
    }

    public static Monitor createMonitor(List<Task> taskList) {
        Monitor monitor = new Monitor();
        for (Task task : taskList) {
            monitor.getTask().add(task);
        }
        return monitor;
    }

    public static Config createConfig(List<Parameter> parameters) {
        Config config = new Config();
        for (Parameter parameter : parameters) {
            if (!parameter.isSecure()) {
                config.getParameter().add(parameter);
            }
        }
        return config;
    }

    static String dateAsString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return javax.xml.bind.DatatypeConverter.printDateTime(calendar);
    }

    public static Logs createLogs(ProcessLog... processLogs) {
        Logs logs = new Logs();
        for (ProcessLog processLog : processLogs) {
            logs.getLog().add(createLog(processLog));
        }
        return logs;
    }

    public static Log createLog(ProcessLog processLog) {
        Log log = new Log();
        log.setId(processLog.getId());
        log.setContent(String.format(LOGCONTENT_URI, processLog.getId()));
        log.setFormat(Constants.SLP_LOG_FORMAT_TEXT);
        return log;
    }

}
