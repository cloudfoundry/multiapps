package com.sap.cloud.lm.sl.slp.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiParameter.ParameterWithMetadata;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata.ParameterType;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.SlpParameterType;

import junit.framework.AssertionFailedError;

public class ActivitiParameterTest {

    private static final String PARAM_NUMBER_OF_RETRIES = "numberOfRetries";
    private static final String PARAM_CONFIRM = "confirm";
    private static final String PARAM_SID = "SID";
    private static final String PARAM_REQUIRED_WITH_DEFAULT = "requiredDefault";
    private static final String PARAM_OPTIONAL_WITH_DEFAULT = "optionalDefault";
    private static final String PARAM_FILE_LIST = "fileList";

    private static final ParameterMetadata PARAM_SID_METADATA = ParameterMetadata.builder().id(PARAM_SID).required(true).type(
        ParameterType.STRING).build();
    private static final ParameterMetadata PARAM_CONFIRM_METADATA = ParameterMetadata.builder().id(PARAM_CONFIRM).required(true).type(
        ParameterType.BOOLEAN).build();
    private static final ParameterMetadata PARAM_NUMBER_OF_RETRIES_METADATA = ParameterMetadata.builder().id(PARAM_NUMBER_OF_RETRIES).type(
        ParameterType.INTEGER).build();
    private static final ParameterMetadata PARAM_REQUIRED_WITH_DEFAULT_METADATA = ParameterMetadata.builder().id(
        PARAM_REQUIRED_WITH_DEFAULT).required(true).defaultValue("test1").build();
    private static final ParameterMetadata PARAM_OPTIONAL_WITH_DEFAULT_METADATA = ParameterMetadata.builder().id(
        PARAM_OPTIONAL_WITH_DEFAULT).defaultValue("test2").build();
    private static final ParameterMetadata PARAM_FILE_LIST_METADATA = ParameterMetadata.builder().id(PARAM_FILE_LIST).required(false).type(
        ParameterType.TABLE).build();

    private List<ParameterMetadata> parametersMetadata;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        parametersMetadata = new ArrayList<ParameterMetadata>();
        parametersMetadata.add(PARAM_SID_METADATA);
        parametersMetadata.add(PARAM_CONFIRM_METADATA);
        parametersMetadata.add(PARAM_NUMBER_OF_RETRIES_METADATA);
        parametersMetadata.add(PARAM_REQUIRED_WITH_DEFAULT_METADATA);
        parametersMetadata.add(PARAM_OPTIONAL_WITH_DEFAULT_METADATA);
        parametersMetadata.add(PARAM_FILE_LIST_METADATA);
    }

    @Test
    public void testGetVariables() throws Exception {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(PARAM_SID, "ABC"));
        parameters.add(SlpObjectFactory.createParameter(PARAM_CONFIRM, "true"));
        parameters.add(SlpObjectFactory.createParameter(PARAM_NUMBER_OF_RETRIES, "7"));

        Map<String, Object> variables = ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, false, true);
        assertEquals("ABC", variables.get(PARAM_SID));
        assertTrue((Boolean) variables.get(PARAM_CONFIRM));
        assertEquals("test1", variables.get(PARAM_REQUIRED_WITH_DEFAULT));
        assertEquals("test2", variables.get(PARAM_OPTIONAL_WITH_DEFAULT));
    }

    @Test
    public void testGetDefaultValueParameters() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(PARAM_SID, "foo"));
        Map<String, Object> variables = ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, false, false);
        assertEquals("foo", variables.get(PARAM_SID));
        assertNull(variables.get(PARAM_REQUIRED_WITH_DEFAULT));
        assertFalse(variables.containsKey(PARAM_REQUIRED_WITH_DEFAULT));

        assertNull(variables.get(PARAM_OPTIONAL_WITH_DEFAULT));
        assertFalse(variables.containsKey(PARAM_OPTIONAL_WITH_DEFAULT));
    }

    @Test
    public void testGetIntegerValue() throws Exception {
        expectedEx.expect(SLException.class);
        expectedEx.expectMessage("The value \"7a\" of parameter with id \"numberOfRetries\"");
        Parameter parameter = SlpObjectFactory.createParameter(PARAM_NUMBER_OF_RETRIES, "7a");
        ParameterWithMetadata parameterWithMetadata = new ParameterWithMetadata(parameter, PARAM_NUMBER_OF_RETRIES_METADATA);
        parameterWithMetadata.getValueOrDefault();
    }

    @Test
    public void testGetBoolenValue() throws Exception {
        expectedEx.expect(SLException.class);
        expectedEx.expectMessage("The value \"7a\" of parameter with id \"confirm\" is not valid. Valid values are true and false");
        Parameter parameter = SlpObjectFactory.createParameter(PARAM_CONFIRM, "7a");
        ParameterWithMetadata parameterWithMetadata = new ParameterWithMetadata(parameter, PARAM_CONFIRM_METADATA);
        parameterWithMetadata.getValueOrDefault();
    }

    @Test
    public void whenGetVariablesWithMissingRequiredParameterAndFailOnMissing_thenExceptionIsThrown() throws Exception {
        expectedEx.expect(SLException.class);
        expectedEx.expectMessage(MessageFormat.format(Messages.REQUIRED_PARAMETERS_ARE_MISSING, Arrays.asList(PARAM_SID)));

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(PARAM_CONFIRM, "true"));
        ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, true, true);
    }

    @Test
    public void whenGetVariablesWithMultipleMissingRequiredParametersAndFailOnMissing_thenExceptionIsThrown() throws Exception {
        expectedEx.expect(SLException.class);
        expectedEx.expectMessage(MessageFormat.format(Messages.REQUIRED_PARAMETERS_ARE_MISSING, Arrays.asList(PARAM_SID, PARAM_CONFIRM)));

        ActivitiParameter.getActivitiVariables(Collections.<Parameter> emptyList(), parametersMetadata, true, true);
    }

    @Test
    public void whenGetVariablesWithUnrecognizedParameter_thenExceptionIsThrown() throws Exception {
        String unrecognizedId = "unrecognizedId";

        expectedEx.expect(SLException.class);
        expectedEx.expectMessage(MessageFormat.format(Messages.UNRECOGNIZED_PARAMETERS, Arrays.asList(unrecognizedId)));

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(unrecognizedId, "true"));
        ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, false, true);
    }

    @Test
    public void whenGetVariablesWithMultipleUnrecognizedParameters_thenExceptionIsThrown() throws Exception {
        String unrecognizedId1 = "unrecognizedId1";
        String unrecognizedId2 = "unrecognizedId2";

        expectedEx.expect(SLException.class);
        expectedEx.expectMessage(MessageFormat.format(Messages.UNRECOGNIZED_PARAMETERS, Arrays.asList(unrecognizedId1, unrecognizedId2)));

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(unrecognizedId1, "true"));
        parameters.add(SlpObjectFactory.createParameter(unrecognizedId2, "true"));
        ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, false, true);
    }

    @Test
    public void whenGetVariablesWithMissingRequiredParameterAndNoFailOnMissing_thenSuccess() throws Exception {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(SlpObjectFactory.createParameter(PARAM_CONFIRM, "true"));
        ActivitiParameter.getActivitiVariables(parameters, parametersMetadata, false, true);
    }

    @Test
    public void tesGetParametersWithValues() throws Exception {
        VariableHandler variableHandler = new VariableHandler() {

            private Map<String, Object> values;

            {
                values = new HashMap<String, Object>();
                values.put(PARAM_SID, "ABC");
                values.put(PARAM_CONFIRM, true);
                values.put(PARAM_NUMBER_OF_RETRIES, 2);
                values.put(PARAM_REQUIRED_WITH_DEFAULT, null);
            }

            @Override
            public Object getVariable(String variableName) {
                return values.get(variableName);
            }

            @Override
            public List<Object> getVariablesIncludingFromSubProcesses(String variableName) {
                return Arrays.asList(getVariable(variableName));
            }
        };

        List<Parameter> params = ActivitiParameter.getParameters(parametersMetadata, variableHandler);
        assertParameter(params, PARAM_SID, "ABC", null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_CONFIRM, "true", null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_NUMBER_OF_RETRIES, "2", null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_REQUIRED_WITH_DEFAULT, null, "test1", SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_OPTIONAL_WITH_DEFAULT, null, "test2", SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_FILE_LIST, null, null, SlpParameterType.SLP_PARAMETER_TYPE_TABLE);
    }

    private void assertParameter(List<Parameter> parameters, String id, String value, String defaultValue, SlpParameterType type) {
        for (Parameter param : parameters) {
            if (id.equals(param.getId())) {
                assertEquals(value, param.getValue());
                assertEquals(defaultValue, param.getDefault());
                assertEquals(type, param.getType());
                return;
            }
        }
        throw new AssertionFailedError(String.format("Parameter \"%s\" does not exist", id));
    }

    @Test
    public void testGetParameters() throws Exception {
        List<Parameter> params = ActivitiParameter.getParameters(parametersMetadata);
        assertParameter(params, PARAM_SID, null, null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_CONFIRM, null, null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_NUMBER_OF_RETRIES, null, null, SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_REQUIRED_WITH_DEFAULT, null, "test1", SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_OPTIONAL_WITH_DEFAULT, null, "test2", SlpParameterType.SLP_PARAMETER_TYPE_SCALAR);
        assertParameter(params, PARAM_FILE_LIST, null, null, SlpParameterType.SLP_PARAMETER_TYPE_TABLE);
    }

}
