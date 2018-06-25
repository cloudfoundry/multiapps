package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v3_1.ResourceParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Resource extends com.sap.cloud.lm.sl.mta.model.v3_0.Resource
    implements ParametersWithMetadataContainer, PropertiesWithMetadataContainer {

    @YamlElement(ResourceParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata;
    @YamlElement(ResourceParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;
    @YamlElement(ResourceParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies3_1;
    private boolean isOptional;

    protected Resource() {
    }

    @Override
    public void setPropertiesMetadata(Metadata propertiesMetadata) {
        this.propertiesMetadata = propertiesMetadata;
    }

    @Override
    public Metadata getPropertiesMetadata() {
        return propertiesMetadata;
    }

    @Override
    public void setParametersMetadata(Metadata parametersMetadata) {
        this.parametersMetadata = parametersMetadata;
    }

    @Override
    public Metadata getParametersMetadata() {
        return parametersMetadata;
    }

    public void setRequiredDependencies3_1(List<RequiredDependency> requiredDependencies3_1) {
        setRequiredDependencies(requiredDependencies3_1);
    }

    protected void setRequiredDependencies(List<? extends RequiredDependency> requiredDependencies) {
        this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
    }

    public List<RequiredDependency> getRequiredDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_1;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    @Override
    public Resource copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        result.setPropertiesMetadata(getPropertiesMetadata());
        result.setParametersMetadata(getParametersMetadata());
        result.setRequiredDependencies(getRequiredDependencies3_1());
        result.setOptional(isOptional());
        return result.build();
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        super.accept(context, visitor);
        for (RequiredDependency requiredDependency : getRequiredDependencies()) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v3_0.Resource.Builder {

        protected Metadata propertiesMetadata;
        protected Metadata parametersMetadata;
        private List<RequiredDependency> requiredDependencies;
        private Boolean isOptional;

        @Override
        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setPropertiesMetadata(propertiesMetadata);
            result.setParametersMetadata(parametersMetadata);
            result.setRequiredDependencies3_1(requiredDependencies);
            result.setOptional(getOrDefault(isOptional, false));
            return result;
        }

        public void setPropertiesMetadata(Metadata propertiesMetadata) {
            this.propertiesMetadata = propertiesMetadata;
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

        public void setRequiredDependencies(List<RequiredDependency> requiredDependencies) {
            this.requiredDependencies = requiredDependencies;
        }

        public void setOptional(Boolean isOptional) {
            this.isOptional = isOptional;
        }

    }

}
