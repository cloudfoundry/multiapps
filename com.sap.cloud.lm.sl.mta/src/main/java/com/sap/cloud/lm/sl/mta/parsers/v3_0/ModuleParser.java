package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.Module;
import com.sap.cloud.lm.sl.mta.model.v3_0.Module.Builder;
import com.sap.cloud.lm.sl.mta.model.v3_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.ModuleParser {

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
        builder.setRequiredDependencies3_0(getRequiredDependencies3_0());
        builder.setProvidedDependencies3_0(getProvidedDependencies3_0());
        return builder.build();
    }

    protected List<ProvidedDependency> getProvidedDependencies3_0() throws ParsingException {
        return ListUtil.cast(getProvidedDependencies2_0());
    }

    @Override
    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v3
    }

    protected List<RequiredDependency> getRequiredDependencies3_0() throws ParsingException {
        return ListUtil.cast(getRequiredDependencies2_0());
    }

    @Override
    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // v3
    }
}
