package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.MTAEXT;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionDescriptor.ExtensionDescriptorBuilder;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionDescriptorParser {

    public static final String PARAMETERS = "parameters";
    public static final String DEPLOY_TARGETS = "targets";

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
        // "target-platforms" parameter is kept in version 2.* for backwards compatibility and removed in versions 3.*
        builder.setDeployTargets(getTargetPLatforms());
        if (getDeployTargets() != null) {
            builder.setDeployTargets(getDeployTargets());

        }
        builder.setModules2_0(getModules2_0());
        builder.setResources2_0(getResources2_0());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected List<String> getDeployTargets() {
        return getListElement(DEPLOY_TARGETS);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionResource> getResources2_0() throws ParsingException {
        return ListUtil.cast(getResources1_0());
    }

    @Override
    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v2
    }

    protected List<ExtensionModule> getModules2_0() throws ParsingException {
        return ListUtil.cast(getModules1_0());
    }

    @Override
    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v2
    }

}
