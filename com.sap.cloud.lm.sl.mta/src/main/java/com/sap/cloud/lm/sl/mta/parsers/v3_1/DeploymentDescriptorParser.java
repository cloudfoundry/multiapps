package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.MTAD;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class DeploymentDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.DeploymentDescriptorParser {

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
        builder.setModules3_1(getModules3_1());
        builder.setResources3_1(getResources3_1());
        builder.setParameters(getParameters());
        builder.setParametersMetadata(getParametersMetadata());
        return builder.build();
    }

    @Override
    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source); // v3_1
    }

    @Override
    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source); // v3_1
    }

    protected List<Resource> getResources3_1() throws SLException {
        return ListUtil.cast(getResources3_0());
    }

    protected List<Module> getModules3_1() throws SLException {
        return ListUtil.cast(getModules3_0());
    }

    protected Metadata getParametersMetadata() throws SLException {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

}
