package com.sap.cloud.lm.sl.slp.activiti;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.VariableHandler;
import com.sap.cloud.lm.sl.slp.model.converter.SlpParameterTablevalueToMapTypeConverter;
import com.sap.lmsl.slp.Parameter;

public class ActivitiParameter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiParameter.class);

    static class ParameterWithMetadata {

        private Parameter parameter;
        private ParameterMetadata parameterMetadata;

        public ParameterWithMetadata(Parameter parameter, ParameterMetadata parameterMetadata) {
            this.parameter = parameter;
            this.parameterMetadata = parameterMetadata;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public ParameterMetadata getParameterMetadata() {
            return parameterMetadata;
        }

        public Object getValueOrDefault() {
            Object value = getValue();
            if (value != null) {
                return value;
            }
            Object defaultValue = parameterMetadata.getDefaultValue();
            LOGGER.info(MessageFormat.format(Messages.SETTING_DEFAULT_VALUE_FOR_PARAMETER, defaultValue, parameterMetadata.getId()));
            return defaultValue;
        }

        private Object getValue() throws SLException {
            if (parameter == null) {
                return null;
            }
            switch (parameterMetadata.getType()) {
                case STRING:
                    return parameter.getValue();
                case INTEGER:
                    try {
                        return Integer.valueOf(parameter.getValue());
                    } catch (NumberFormatException e) {
                        throw new SLException(e,
                            MessageFormat.format(Messages.PARAMETER_VALUE_IS_NOT_INTEGER, parameter.getValue(), parameter.getId()));
                    }
                case BOOLEAN: {
                    String value = parameter.getValue();
                    if ("true".equals(value)) {
                        return true;
                    } else if ("false".equals(value)) {
                        return false;
                    } else {
                        throw new SLException(
                            MessageFormat.format(Messages.PARAMETER_VALUE_IS_NOT_BOOLEAN, parameter.getValue(), parameter.getId()));
                    }
                }
                case TABLE:
                    try {
                        SlpParameterTablevalueToMapTypeConverter converter = new SlpParameterTablevalueToMapTypeConverter();
                        return converter.convertForward(parameter.getTablevalue());
                    } catch (IllegalArgumentException e) {
                        String parameterValue = parameter != null ? parameter.getValue() : "";
                        String parameterId = parameter != null ? parameter.getId() : "";
                        throw new SLException(e, MessageFormat.format(Messages.PARAMETER_VALUE_IS_INVALID, parameterValue, parameterId));
                    }
                default:
                    throw new IllegalArgumentException(
                        MessageFormat.format(Messages.PARAMETER_TYPE_NOT_SUPPORTED, parameterMetadata.getType().toString()));
            }
        }

    }

    static Map<String, Object> getActivitiVariables(List<Parameter> parameters, Collection<ParameterMetadata> parametersMetadata,
        boolean failOnMissingRequiredParameters, boolean setDefaultValues) throws SLException {
        List<ParameterWithMetadata> parametersWithMetadata = mergeParametersAndMetadata(parameters, parametersMetadata);

        validateThereAreNoUnrecognizedParameters(parametersWithMetadata);

        Map<String, Object> variables = new HashMap<>();
        for (ParameterWithMetadata parameterWithMetadata : parametersWithMetadata) {
            if (setDefaultValues) {
                variables.put(parameterWithMetadata.getParameterMetadata().getId(), parameterWithMetadata.getValueOrDefault());
            } else if (parameterWithMetadata.getValue() != null) {
                variables.put(parameterWithMetadata.getParameterMetadata().getId(), parameterWithMetadata.getValue());
            }
        }
        if (failOnMissingRequiredParameters) {
            validateThereAreNoMissingRequiredParameters(variables, parametersMetadata);
        }
        return variables;
    }

    private static List<ParameterWithMetadata> mergeParametersAndMetadata(Collection<Parameter> parameters,
        Collection<ParameterMetadata> parametersMetadata) {
        List<ParameterWithMetadata> result = new ArrayList<>();
        List<Parameter> unprocessedParameters = new LinkedList<>(parameters);
        for (ParameterMetadata parameterMetadata : parametersMetadata) {
            Parameter parameter = removeParameter(unprocessedParameters, parameterMetadata.getId());
            ParameterWithMetadata parameterWithMetadata = new ParameterWithMetadata(parameter, parameterMetadata);
            result.add(parameterWithMetadata);
        }
        for (Parameter unprocessedParameter : unprocessedParameters) {
            result.add(new ParameterWithMetadata(unprocessedParameter, null));
        }
        return result;
    }

    private static Parameter removeParameter(Collection<Parameter> parameters, String id) {
        for (Iterator<Parameter> iterator = parameters.iterator(); iterator.hasNext();) {
            Parameter parameter = iterator.next();
            if (parameter.getId().equals(id)) {
                iterator.remove();
                return parameter;
            }
        }
        return null;
    }

    private static void validateThereAreNoUnrecognizedParameters(List<ParameterWithMetadata> parametersWithMetadata) {
        Set<String> unrecognizedParameters = new TreeSet<>();
        for (ParameterWithMetadata parameterWithMetadata : parametersWithMetadata) {
            if (parameterWithMetadata.getParameterMetadata() == null) {
                unrecognizedParameters.add(parameterWithMetadata.getParameter().getId());
            }
        }
        if (!unrecognizedParameters.isEmpty()) {
            throw new SLException(Messages.UNRECOGNIZED_PARAMETERS, unrecognizedParameters);
        }
    }

    private static void validateThereAreNoMissingRequiredParameters(Map<String, Object> variables,
        Collection<ParameterMetadata> parametersMetadata) {
        Set<String> missingRequiredParameters = new TreeSet<>();
        for (ParameterMetadata parameterMetadata : parametersMetadata) {
            if (parameterMetadata.isRequired() && variables.get(parameterMetadata.getId()) == null) {
                missingRequiredParameters.add(parameterMetadata.getId());
            }
        }
        if (!missingRequiredParameters.isEmpty()) {
            throw new SLException(Messages.REQUIRED_PARAMETERS_ARE_MISSING, missingRequiredParameters);
        }
    }

    private static Parameter getParameter(ParameterMetadata parameterMetadata, Object value) {
        switch (parameterMetadata.getType()) {
            case STRING:
            case BOOLEAN:
            case INTEGER:
                return SlpObjectFactory.createParameter(parameterMetadata.getId(), value, parameterMetadata.getDefaultValue(),
                    parameterMetadata.isRequired(), parameterMetadata.isSecure());
            case TABLE:
                @SuppressWarnings("unchecked")
                List<Map<String, Map<String, Object>>> map = (List<Map<String, Map<String, Object>>>) value;
                SlpParameterTablevalueToMapTypeConverter converter = new SlpParameterTablevalueToMapTypeConverter();
                return SlpObjectFactory.createParameter(parameterMetadata.getId(), converter.convertBackward(map),
                    parameterMetadata.isRequired(), parameterMetadata.isSecure());
            default:
                throw new IllegalArgumentException(
                    MessageFormat.format(Messages.PARAMETER_TYPE_NOT_SUPPORTED, parameterMetadata.getType().toString()));
        }
    }

    static List<Parameter> getParameters(Collection<ParameterMetadata> parametersMetadata) {
        return getParameters(parametersMetadata, null);
    }

    static List<Parameter> getParameters(Collection<ParameterMetadata> parametersMetadata, VariableHandler variableHandler) {
        List<Parameter> parameters = new ArrayList<Parameter>();
        for (ParameterMetadata parameterMetadata : parametersMetadata) {
            Object value = variableHandler != null ? variableHandler.getVariable(parameterMetadata.getId()) : null;
            parameters.add(getParameter(parameterMetadata, value));
        }
        return parameters;
    }

}
