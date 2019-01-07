package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.MTAD;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.Module;
import com.sap.cloud.lm.sl.mta.model.v3.Resource;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor.Builder;
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
        Builder builder = new Builder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setVersion(getVersion());
        builder.setProvider(getProvider());
        builder.setCopyright(getCopyright());
        builder.setSchemaVersion(getSchemaVersion());
        builder.setModules3(getModules3());
        builder.setResources3(getResources3());
        builder.setParameters(getParameters());
        builder.setParametersMetadata(getParametersMetadata());
        return builder.build();
    }

    protected List<Resource> getResources3() {
        return ListUtil.cast(getResources2());
    }

    @Override
    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source); // v3
    }

    protected List<Module> getModules3() {
        return ListUtil.cast(getModules2());
    }

    @Override
    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source); // v3
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

}
