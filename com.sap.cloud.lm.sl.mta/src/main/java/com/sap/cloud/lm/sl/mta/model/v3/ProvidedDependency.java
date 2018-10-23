package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.parsers.v3.ProvidedDependencyParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ProvidedDependency extends com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency
    implements PropertiesWithMetadataContainer, ParametersContainer, ParametersWithMetadataContainer {

    @YamlElement(ProvidedDependencyParser.PARAMETERS)
    private Map<String, Object> parameters;
    @YamlElement(ProvidedDependencyParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata;
    @YamlElement(ProvidedDependencyParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;

    protected ProvidedDependency() {

    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public Metadata getPropertiesMetadata() {
        return propertiesMetadata;
    }

    @Override
    public Metadata getParametersMetadata() {
        return parametersMetadata;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void setPropertiesMetadata(Metadata metadata) {
        this.propertiesMetadata = metadata;
    }

    @Override
    public void setParametersMetadata(Metadata metadata) {
        this.parametersMetadata = metadata;
    }

    @Override
    public ProvidedDependency copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setPublic(isPublic());
        result.setProperties(getProperties());
        result.setPropertiesMetadata(getPropertiesMetadata());
        result.setParameters(getParameters());
        result.setParametersMetadata(getParametersMetadata());
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency.Builder {

        private Map<String, Object> parameters;
        private Metadata propertiesMetadata;
        private Metadata parametersMetadata;

        @Override
        public ProvidedDependency build() {
            ProvidedDependency result = new ProvidedDependency();
            result.setName(name);
            result.setPublic(ObjectUtils.defaultIfNull(isPublic, false));
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setPropertiesMetadata(ObjectUtils.defaultIfNull(propertiesMetadata, Metadata.DEFAULT_METADATA));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setParametersMetadata(ObjectUtils.defaultIfNull(parametersMetadata, Metadata.DEFAULT_METADATA));
            return result;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public void setPropertiesMetadata(Metadata metadata) {
            this.propertiesMetadata = metadata;
        }

        public void setParametersMetadata(Metadata metadata) {
            this.parametersMetadata = metadata;
        }

    }

}
