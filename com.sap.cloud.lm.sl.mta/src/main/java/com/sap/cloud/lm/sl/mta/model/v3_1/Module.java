package com.sap.cloud.lm.sl.mta.model.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.ParametersWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.model.PropertiesWithMetadataContainer;
import com.sap.cloud.lm.sl.mta.parsers.v3_1.ModuleParser;
import com.sap.cloud.lm.sl.mta.util.MetadataConverter;
import com.sap.cloud.lm.sl.mta.util.YamlAdapter;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class Module extends com.sap.cloud.lm.sl.mta.model.v3_0.Module
    implements ParametersWithMetadataContainer, PropertiesWithMetadataContainer {

    @YamlElement(ModuleParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies3_1;
    @YamlElement(ModuleParser.PROVIDES)
    private List<ProvidedDependency> providedDependencies3_1;
    @YamlElement(ModuleParser.PROPERTIES_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata propertiesMetadata;
    @YamlElement(ModuleParser.PARAMETERS_METADATA)
    @YamlAdapter(MetadataConverter.class)
    private Metadata parametersMetadata;

    protected Module() {

    }

    public List<RequiredDependency> getRequiredDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getRequiredDependencies());
    }

    @Override
    protected List<? extends RequiredDependency> getRequiredDependencies() {
        return requiredDependencies3_1;
    }

    public List<ProvidedDependency> getProvidedDependencies3_1() {
        return ListUtil.upcastUnmodifiable(getProvidedDependencies());
    }

    @Override
    protected List<? extends ProvidedDependency> getProvidedDependencies() {
        return providedDependencies3_1;
    }

    public void setRequiredDependencies3_1(List<RequiredDependency> requiredDependencies) {
        setRequiredDependencies(requiredDependencies);
    }

    @Override
    protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
        this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
    }

    public void setProvidedDependencies3_1(List<ProvidedDependency> providedDependencies) {
        setProvidedDependencies(providedDependencies);
    }

    @Override
    protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
        this.providedDependencies3_1 = ListUtil.cast(providedDependencies);
    }

    public Metadata getPropertiesMetadata() {
        return propertiesMetadata;
    }

    public Metadata getParametersMetadata() {
        return parametersMetadata;
    }

    public void setPropertiesMetadata(Metadata propertiesMetadata) {
        this.propertiesMetadata = propertiesMetadata;
    }

    public void setParametersMetadata(Metadata parametersMetadata) {
        this.parametersMetadata = parametersMetadata;
    }

    public Module copyOf() {
        ModuleBuilder result = new ModuleBuilder();
        result.setName(getName());
        result.setType(getType());
        result.setPath(getPath());
        result.setDescription(getDescription());
        result.setProperties(getProperties());
        result.setParameters(getParameters());
        List<RequiredDependency> clonedRequiredDependencies = new ArrayList<>();
        for (RequiredDependency requiredDependency : getRequiredDependencies3_1()) {
            clonedRequiredDependencies.add(requiredDependency.copyOf());
        }
        result.setRequiredDependencies3_1(clonedRequiredDependencies);
        List<ProvidedDependency> clonedProvidedDependencies = new ArrayList<>();
        for (ProvidedDependency providedDependency : getProvidedDependencies3_1()) {
            clonedProvidedDependencies.add(providedDependency.copyOf());
        }
        result.setProvidedDependencies3_1(clonedProvidedDependencies);
        result.setParametersMetadata(getParametersMetadata());
        result.setPropertiesMetadata(getPropertiesMetadata());
        return result.build();
    }

    public static class ModuleBuilder extends com.sap.cloud.lm.sl.mta.model.v3_0.Module.ModuleBuilder {

        protected List<RequiredDependency> requiredDependencies3_1;
        protected List<ProvidedDependency> providedDependencies3_1;
        protected Metadata propertiesMetadata;
        protected Metadata parametersMetadata;

        @Override
        public Module build() {
            Module result = new Module();
            result.setName(name);
            result.setType(type);
            result.setPath(path);
            result.setDescription(description);
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            result.setParameters(getOrDefault(parameters, Collections.<String, Object> emptyMap()));
            result.setRequiredDependencies3_1(getOrDefault(requiredDependencies3_1, Collections.<RequiredDependency> emptyList()));
            result.setProvidedDependencies3_1(getOrDefault(providedDependencies3_1, Collections.<ProvidedDependency> emptyList()));
            result.setPropertiesMetadata(propertiesMetadata);
            result.setParametersMetadata(parametersMetadata);
            return result;
        }

        protected void setRequiredDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency> requiredDependencies) {
            this.requiredDependencies3_1 = ListUtil.cast(requiredDependencies);
        }

        public void setRequiredDependencies3_1(List<RequiredDependency> requiredDependencies) {
            setRequiredDependencies(requiredDependencies);
        }

        @Override
        protected void setProvidedDependencies(List<? extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency> providedDependencies) {
            this.providedDependencies3_1 = ListUtil.cast(providedDependencies);
        }

        public void setProvidedDependencies3_1(List<ProvidedDependency> providedDependencies) {
            setProvidedDependencies(providedDependencies);
        }

        public void setPropertiesMetadata(Metadata propertiesMetadata) {
            this.propertiesMetadata = propertiesMetadata;
        }

        public void setParametersMetadata(Metadata parametersMetadata) {
            this.parametersMetadata = parametersMetadata;
        }

    }

}
