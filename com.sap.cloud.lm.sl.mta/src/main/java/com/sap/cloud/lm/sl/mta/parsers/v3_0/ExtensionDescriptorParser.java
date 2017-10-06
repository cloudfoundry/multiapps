package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.MTAEXT;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionDescriptor.ExtensionDescriptorBuilder;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.ExtensionDescriptorParser {

    public static final String DESCRIPTION = "description";

    public ExtensionDescriptorParser(Map<String, Object> source) {
        super(MTAEXT, source);
    }

    protected ExtensionDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionDescriptor parse() throws ParsingException {
        ExtensionDescriptorBuilder builder = new ExtensionDescriptorBuilder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setParentId(getParentId());
        builder.setProvider(getProvider());
        builder.setSchemaVersion(getSchemaVersion());
        builder.setDeployTargets(getDeployTargets());
        builder.setModules3_0(getModules3_0());
        builder.setResources3_0(getResources3_0());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected List<ExtensionResource> getResources3_0() throws ParsingException {
        return ListUtil.cast(getResources2_0());
    }

    @Override
    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v3
    }

    protected List<ExtensionModule> getModules3_0() throws ParsingException {
        return ListUtil.cast(getModules2_0());
    }

    @Override
    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v3
    }

    @Override
    protected String getDescription() throws ParsingException {
        return getStringElement(DESCRIPTION);
    }

}
