package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.MTAEXT;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser {

    public static final String DESCRIPTION = "description";

    public ExtensionDescriptorParser(Map<String, Object> source) {
        super(MTAEXT, source);
    }

    protected ExtensionDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionDescriptor parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setParentId(getParentId());
        builder.setProvider(getProvider());
        builder.setSchemaVersion(getSchemaVersion());
        builder.setDeployTargets(getDeployTargets());
        builder.setModules3(getModules3());
        builder.setResources3(getResources3());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected List<ExtensionResource> getResources3() {
        return ListUtil.cast(getResources2());
    }

    @Override
    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v3
    }

    protected List<ExtensionModule> getModules3() {
        return ListUtil.cast(getModules2());
    }

    @Override
    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v3
    }

    @Override
    protected String getDescription() {
        return getStringElement(DESCRIPTION);
    }

}
