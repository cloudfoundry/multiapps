package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.parsers.v3.ResourceParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Resource extends com.sap.cloud.lm.sl.mta.model.v2.Resource
    implements ParametersWithMetadataContainer, PropertiesWithMetadataContainer {

    @YamlElement(ResourceParser.ACTIVE)
    private boolean isActive;
    @YamlElement(ResourceParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata;
    @YamlElement(ResourceParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;
    @YamlElement(ResourceParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies3;
    @YamlElement(ResourceParser.OPTIONAL)
    private boolean isOptional;

    protected Resource() {

    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public Metadata getPropertiesMetadata() {
        return propertiesMetadata;
    }

    @Override
    public Metadata getParametersMetadata() {
        return parametersMetadata;
    }

    public List<RequiredDependency> getRequiredDependencies3() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies3;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public void setPropertiesMetadata(Metadata propertiesMetadata) {
        this.propertiesMetadata = propertiesMetadata;
    }

    @Override
    public void setParametersMetadata(Metadata parametersMetadata) {
        this.parametersMetadata = parametersMetadata;
    }

    public void setRequiredDependencies3(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    protected void setRequiredDependencies(List<? extends RequiredDependency> requiredDependencies) {
        this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    @Override
    public Resource copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setActive(isActive());
        result.setType(getType());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        result.setPropertiesMetadata(getPropertiesMetadata());
        result.setParametersMetadata(getParametersMetadata());
        result.setRequiredDependencies3(getRequiredDependencies3());
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

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.Resource.Builder {

        protected Boolean isActive;
        protected Metadata propertiesMetadata;
        protected Metadata parametersMetadata;
        private List<RequiredDependency> requiredDependencies3;
        private Boolean isOptional;

        @Override
        public Resource build() {
            Resource result = new Resource();
            result.setName(name);
            result.setType(type);
            result.setActive(ObjectUtils.defaultIfNull(isActive, true));
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setPropertiesMetadata(propertiesMetadata);
            result.setParametersMetadata(parametersMetadata);
            result.setRequiredDependencies3(
                ObjectUtils.defaultIfNull(requiredDependencies3, Collections.<RequiredDependency> emptyList()));
            result.setOptional(ObjectUtils.defaultIfNull(isOptional, false));
            return result;
        }

        public void setActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public void setPropertiesMetadata(Metadata propertiesMetadata) {
            this.propertiesMetadata = propertiesMetadata;
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

        public void setRequiredDependencies3(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
            this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
        }

        public void setOptional(Boolean isOptional) {
            this.isOptional = isOptional;
        }

    }

}
