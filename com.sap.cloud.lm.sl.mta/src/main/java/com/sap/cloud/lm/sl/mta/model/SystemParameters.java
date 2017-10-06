package com.sap.cloud.lm.sl.mta.model;

import java.util.Map;

import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.model.json.PropertiesAdapterFactory;

public class SystemParameters {

    @JsonAdapter(PropertiesAdapterFactory.class)
    private final Map<String, Map<String, Object>> moduleParameters;
    @JsonAdapter(PropertiesAdapterFactory.class)
    private final Map<String, Object> generalParameters;
    @JsonAdapter(PropertiesAdapterFactory.class)
    private final Map<String, Map<String, Object>> resourceParameters;
    @JsonAdapter(PropertiesAdapterFactory.class)
    private final Map<String, String> singularPluralMapping;

    public SystemParameters(Map<String, Object> generalParameters, Map<String, Map<String, Object>> moduleParameters,
        Map<String, Map<String, Object>> resourceParameters, Map<String, String> singularPluralMapping) {
        this.generalParameters = generalParameters;
        this.moduleParameters = moduleParameters;
        this.resourceParameters = resourceParameters;
        this.singularPluralMapping = singularPluralMapping;
    }

    public Map<String, Map<String, Object>> getModuleParameters() {
        return moduleParameters;
    }

    public Map<String, Object> getGeneralParameters() {
        return generalParameters;
    }

    public Map<String, Map<String, Object>> getResourceParameters() {
        return resourceParameters;
    }

    public Map<String, String> getSingularPluralMapping() {
        return singularPluralMapping;
    }

}
