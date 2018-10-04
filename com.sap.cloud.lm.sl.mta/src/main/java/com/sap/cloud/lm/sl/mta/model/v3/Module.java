package com.sap.cloud.lm.sl.mta.model.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.parsers.v3.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module extends com.sap.cloud.lm.sl.mta.model.v2.Module
    implements ParametersWithMetadataContainer, PropertiesWithMetadataContainer {

    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies3;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies3;
    @YamlElement(ModuleParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata;
    @YamlElement(ModuleParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;

    protected Module() {

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
    public void setPropertiesMetadata(Metadata propertiesMetadata) {
        this.propertiesMetadata = propertiesMetadata;
    }

    @Override
    public void setParametersMetadata(Metadata parametersMetadata) {
        this.parametersMetadata = parametersMetadata;
    }

    public List<RequiredDependency> getRequiredDependencies3() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    @Override
    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies3;
    }

    public List<ProvidedDependency> getProvidedDependencies3() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies3;
    }

    public void setRequiredDependencies3(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
        this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency> providedDependencies) {
        this.providedDependencies3 = ListUtil.cast(providedDependencies);
    }

    @Override
    public Module copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setType(getType());
        result.setPath(getPath());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        List<RequiredDependency> clonedRequiredDependencies = new ArrayList<>();
        for (RequiredDependency requiredDependency : getRequiredDependencies3()) {
            clonedRequiredDependencies.add(requiredDependency.copyOf());
        }
        result.setRequiredDependencies3(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies3()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies3(clonedProvidedDependencies);
        result.setParametersMetadata(getParametersMetadata());
        result.setPropertiesMetadata(getPropertiesMetadata());
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v2.Module.Builder {

        protected List<RequiredDependency> requiredDependencies3;
        protected List<ProvidedDependency> providedDependencies3;
        protected Metadata propertiesMetadata;
        protected Metadata parametersMetadata;

        @Override
        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setPath(path);
            result.setDescription(description);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(ObjectUtils.defaultIfNull(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies3(
                ObjectUtils.defaultIfNull(requiredDependencies3, Collections.<RequiredDependency> emptyList()));
            result.setProvidedDependencies3(
                ObjectUtils.defaultIfNull(providedDependencies3, Collections.<ProvidedDependency> emptyList()));
            result.setPropertiesMetadata(propertiesMetadata);
            result.setParametersMetadata(parametersMetadata);
            return result;
        }

        public void setPropertiesMetadata(Metadata propertiesMetadata) {
            this.propertiesMetadata = propertiesMetadata;
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency> requiredDependencies) {
            this.requiredDependencies3 = ListUtil.cast(requiredDependencies);
        }

        public void setRequiredDependencies3(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        @Override
        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency> providedDependencies) {
            this.providedDependencies3 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies3(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

    }
}
