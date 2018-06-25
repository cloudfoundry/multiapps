package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.MTAD;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_0.DeploymentDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.model.v3_0.Module;
import com.sap.cloud.lm.sl.mta.model.v3_0.Resource;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class DeploymentDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.DeploymentDescriptorParser {

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
        builder.setModules3_0(getModules3_0());
        builder.setResources3_0(getResources3_0());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected List<Resource> getResources3_0() throws ParsingException {
        return ListUtil.cast(getResources2_0());
    }

    @Override
    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source); // v3
    }

    protected List<Module> getModules3_0() throws ParsingException {
        return ListUtil.cast(getModules2_0());
    }

    @Override
    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source); // v3
    }

}
