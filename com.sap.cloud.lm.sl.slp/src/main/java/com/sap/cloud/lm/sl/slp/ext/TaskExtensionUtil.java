package com.sap.cloud.lm.sl.slp.ext;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage;
import com.sap.cloud.lm.sl.persistence.model.ProgressMessage.ProgressMessageType;
import com.sap.cloud.lm.sl.persistence.util.ProgressMessageUtil;
import com.sap.cloud.lm.sl.slp.services.TaskExtensionService;

public class TaskExtensionUtil {

    /**
     * Sets task extensions for the specified task. Since task extensions are currently persisted as
     * progress messages, and the processId/taskId combination is not enough to fully identify a
     * task from the SLP point of view (task with the same activiti id can have multiple
     * executions), the progress messages infrastructure relies also on timestamp to make sure that
     * the mapping is correct.
     * 
     * @param processId - the Activiti id of a process id to set extension elements for
     * @param taskId - the Activiti id of a task to set extension elements for
     * @param timestamp - any point of time during the task's execution, it is used to identify
     *        amongst different executions of the same step
     * @param extensionElements - extension elements to step
     * @throws SLException
     */
    public static void setTaskExtensionElements(TaskExtensionService taskExtensionService, String processId, String taskId,
        Timestamp timestamp, Map<String, String> extensionElements) throws SLException {
        String message = JsonUtil.toJson(extensionElements);
        ProgressMessage progressMessage = new ProgressMessage(processId, taskId, ProgressMessageType.EXT, message, timestamp);
        taskExtensionService.add(progressMessage);
    }

    private static Map<? extends String, ? extends String> decode(String stringValue) throws SLException {
        return JsonUtil.convertJsonToMap(stringValue, Map.class);
    }

    public static Map<String, String> decodeExtensionElements(List<String> stringEncodings) throws SLException {
        Map<String, String> result = new HashMap<String, String>();
        for (String value : stringEncodings) {
            result.putAll(decode(value));
        }
        return result;
    }

    public static Map<String, String> getTaskExtensions(List<ProgressMessage> taskExtensions) {
        List<String> progressMessages = ProgressMessageUtil.getProgressMessagesAsString(taskExtensions);
        try {
            return decodeExtensionElements(progressMessages);
        } catch (SLException e) {
            throw new RuntimeException(e);
        }
    }

}
