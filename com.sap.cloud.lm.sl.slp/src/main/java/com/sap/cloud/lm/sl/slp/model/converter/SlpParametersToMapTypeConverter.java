package com.sap.cloud.lm.sl.slp.model.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameters;

public class SlpParametersToMapTypeConverter implements TypeConverter<Parameters, Map<String, Object>> {

    @Override
    public Map<String, Object> convertForward(Parameters parameters) {
        if (parameters.getParameter() != null && parameters.getParameter().size() == 0) {
            return Collections.emptyMap();
        }

        Map<String, Object> result = new HashMap<String, Object>();
        for (Parameter parameter : parameters.getParameter()) {
            readParameterValue(parameter, result);
        }

        return result;
    }

    @Override
    public Parameters convertBackward(Map<String, Object> value) {
        Parameters parameters = new Parameters();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Parameter parameter = new Parameter();
            parameter.setId(entry.getKey());
            if (entry.getValue() instanceof String) {
                parameter.setValue(String.valueOf(entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                List<Map<String, Map<String, Object>>> map = (List<Map<String, Map<String, Object>>>) entry.getValue();
                SlpParameterTablevalueToMapTypeConverter converter = new SlpParameterTablevalueToMapTypeConverter();
                parameter.setTablevalue(converter.convertBackward(map));
            }
            parameters.getParameter().add(parameter);
        }

        return parameters;
    }

    private void readParameterValue(Parameter parameter, Map<String, Object> parametersMap) {
        String value = parameter.getValue();
        String defaultValue = parameter.getDefault();
        if (value == null && defaultValue != null) {
            parametersMap.put(parameter.getId(), defaultValue);
        } else if (parameter.getTablevalue() != null) {
            SlpParameterTablevalueToMapTypeConverter converter = new SlpParameterTablevalueToMapTypeConverter();
            parametersMap.put(parameter.getId(), converter.convertForward(parameter.getTablevalue()));
        } else {
            parametersMap.put(parameter.getId(), value);
        }
    }

}
