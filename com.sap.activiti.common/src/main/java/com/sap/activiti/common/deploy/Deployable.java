package com.sap.activiti.common.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Deployable {

    private String processDefinitionKey;
    private String bpmnFileName;
    private String bpmnFileLocation;

    private static final Logger LOGGER = LoggerFactory.getLogger(Deployable.class);

    /**
     * Constructs a new Deployable by BPMN process file name and the location of the file resource
     * 
     * @param bpmnFileName - simple name of the BPMN file - e.g. "single_step.bpmn"
     * @param pathToBpmnFile - the path to 'bpmnFileName' starting with '/' ('\u002f') - e.g. "/com/sap/activiti/common/cfg"
     */
    public Deployable(String bpmnFileName, String pathToBpmnFile) {
        this.bpmnFileName = bpmnFileName;
        this.bpmnFileLocation = pathToBpmnFile + bpmnFileName;
    }

    public InputStream getBpmnStream() {
        return getClass().getResourceAsStream(bpmnFileLocation);
    }

    public String getProcessDefinitionKey() {
        if (processDefinitionKey == null) {
            processDefinitionKey = getProcessDefinitionKeyFromFile(bpmnFileName);
        }
        return processDefinitionKey;
    }

    public String getBpmnFileName() {
        return bpmnFileName;
    }

    private String getProcessDefinitionKeyFromFile(String bpmnFileName) {
        String processDefinitionKey = null;
        InputStream inputStream = null;

        try {
            inputStream = getBpmnStream();
            String bpmnString = IOUtils.toString(inputStream);
            processDefinitionKey = getProcessDefinitionKeyFromInputStream(bpmnString);
        } catch (IOException e) {
            LOGGER.error("Couldn't convert BPMN stream to string for file: " + bpmnFileName, e);
        } catch (IllegalStateException e) {
            LOGGER.error("Couldn't determine process definition key from the BPMN file: " + bpmnFileName, e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return processDefinitionKey;
    }

    private String getProcessDefinitionKeyFromInputStream(String bpmnString) throws IllegalStateException {
        Pattern pattern = Pattern.compile("<process\\s+[^>]*?id\\s*=\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(bpmnString);
        matcher.find();
        return matcher.group(1);
    }
}
