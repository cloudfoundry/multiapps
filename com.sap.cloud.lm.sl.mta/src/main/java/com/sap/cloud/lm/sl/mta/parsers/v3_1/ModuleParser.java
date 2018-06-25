package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module.Builder;
import com.sap.cloud.lm.sl.mta.model.v3_1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.RequiredDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ModuleParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";

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
        builder.setRequiredDependencies3_1(getRequiredDependencies3_1());
        builder.setProvidedDependencies3_1(getProvidedDependencies3_1());
        builder.setPropertiesMetadata(getPropertiesMetadata());
        builder.setParametersMetadata(getParametersMetadata());
        return builder.build();
    }

    protected List<ProvidedDependency> getProvidedDependencies3_1() throws ParsingException {
        return ListUtil.cast(getProvidedDependencies3_0());
    }

    @Override
    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v3
    }

    protected List<RequiredDependency> getRequiredDependencies3_1() throws ParsingException {
        return ListUtil.cast(getRequiredDependencies3_0());
    }

    @Override
    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // v3
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }
}
