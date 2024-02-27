package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3.Module;
import com.sap.cloud.lm.sl.mta.model.v3.Module.Builder;
import com.sap.cloud.lm.sl.mta.model.v3.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3.RequiredDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ModuleParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";
    public static final String DEPLOYED_AFTER = "deployed-after";

    public ModuleParser(Map<String, Object> source) {
        super(MODULE, source);
    }

    protected ModuleParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Module parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setType(getType());
        builder.setDescription(getDescription());
        builder.setPath(getPath());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setParametersMetadata(getParametersMetadata());
        builder.setRequiredDependencies3(getRequiredDependencies3());
        builder.setProvidedDependencies3(getProvidedDependencies3());
        builder.setDeployedAfter(getDeployedAfter());
        return builder.build();
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

    protected List<ProvidedDependency> getProvidedDependencies3() {
        return ListUtil.cast(getProvidedDependencies2());
    }

    @Override
    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v3
    }

    protected List<RequiredDependency> getRequiredDependencies3() {
        return ListUtil.cast(getRequiredDependencies2());
    }

    @Override
    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // v3
    }

    protected List<String> getDeployedAfter() {
        return getListElement(DEPLOYED_AFTER);
    }
}
