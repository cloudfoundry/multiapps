package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.MTAD;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class DeploymentDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser {

    public static final String PARAMETERS_METADATA = "parameters-metadata";

    public DeploymentDescriptorParser(Map<String, Object> source) {
        super(MTAD, source);
    }

    protected DeploymentDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public DeploymentDescriptor parse() throws ParsingException {
        return super.parse().setParametersMetadata(getParametersMetadata());
    }

    @Override
    public DeploymentDescriptor createEntity() {
        return DeploymentDescriptor.createV3();
    }

    @Override
    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source); // v3
    }

    @Override
    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source); // v3
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

}
