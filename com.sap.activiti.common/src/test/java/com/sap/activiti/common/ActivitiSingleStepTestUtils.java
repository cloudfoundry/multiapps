package com.sap.activiti.common;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

/*
 * This rule needs to be used with com.sap.core.dbaas.automation.activiti.ActivitiTestCfgRuleChain
 * */
public class ActivitiSingleStepTestUtils {
    private static final String DEFAULT_TASK_VALUE = EmptyActivitiStep.class.getName();
    private static final String RESOURCE_PATH = "/com/sap/activiti/common/cfg/";
    private static final String SINGLE_STEP_PROCESS_FILE = "single_step.bpmn";
    public static final String SINGLE_STEP_PROCESS_KEY = "test.provision.hana.singlestep";
    public static final String FULL_PATH = RESOURCE_PATH + SINGLE_STEP_PROCESS_FILE;
    public static final String EXTENSIONS_PLACEHOLDER = "<!-- extensions -->";
    public static final String EXPRESSION_TEMPLATE = "<activiti:field name=\"%s\"><activiti:expression><![CDATA[%s]]></activiti:expression></activiti:field>";
    public static final String EXTENSIONS_OPEN_TAG = "<extensionElements>";
    public static final String EXTENSIONS_CLOSE_TAG = "</extensionElements>";

    public static void deploySingleStepProcess(String stepName) throws Exception {
        deploySingleStepProcess(stepName, null);
    }

    public static void deploySingleStepProcess(String stepName, Map<String, String> expressions) throws Exception {
        deploySingleStepProcess(stepName, ActivitiSingleStepTestUtils.class.getResourceAsStream(FULL_PATH), expressions);
    }

    public static void deploySingleStepProcess(String stepName, InputStream resource, Map<String, String> expressions) throws Exception {
        String bpmn = IOUtils.toString(resource);

        if (expressions != null) {
            bpmn = appendExpressions(expressions, bpmn);
        }
        if (!DEFAULT_TASK_VALUE.equals(stepName)) {
            bpmn = bpmn.replace(DEFAULT_TASK_VALUE, stepName);
        }
        ActivitiTestCfgRuleChain.getActivitiRule()
            .getRepositoryService()
            .createDeployment()
            .addString(FULL_PATH, bpmn)
            .deploy();
    }

    private static String appendExpressions(Map<String, String> expressions, String bpmn) {
        StringBuilder builder = new StringBuilder(EXTENSIONS_OPEN_TAG);
        for (Entry<String, String> expression : expressions.entrySet()) {
            builder.append(String.format(EXPRESSION_TEMPLATE, expression.getKey(), expression.getValue()));
        }
        builder.append(EXTENSIONS_CLOSE_TAG);
        return bpmn.replace(EXTENSIONS_PLACEHOLDER, builder.toString());
    }

    public static void deploySingleStepProcess() throws Exception {
        deploySingleStepProcess(DEFAULT_TASK_VALUE);
    }

    public static void deploySingleStepProcess(InputStream resource) throws Exception {
        deploySingleStepProcess(DEFAULT_TASK_VALUE, resource, null);
    }

    public static String startSingleStepProcess() {
        return startSingleStepProcess(null);
    }

    public static String startSingleStepProcess(String businessKey, Map<String, Object> params) {
        return ActivitiTestCfgRuleChain.startProcess(SINGLE_STEP_PROCESS_KEY, businessKey, params);
    }

    public static String startSingleStepProcess(Map<String, Object> params) {
        return startSingleStepProcess(null, params);
    }
}
