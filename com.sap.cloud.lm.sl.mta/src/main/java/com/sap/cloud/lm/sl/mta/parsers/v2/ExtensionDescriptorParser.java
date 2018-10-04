package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.MTAEXT;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends com.sap.cloud.lm.sl.mta.parsers.v1.ExtensionDescriptorParser {

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
        Builder builder = new Builder();
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
        builder.setModules2(getModules2());
        builder.setResources2(getResources2());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected List<String> getDeployTargets() {
        return getListElement(DEPLOY_TARGETS);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionResource> getResources2() throws ParsingException {
        return ListUtil.cast(getResources1());
    }

    @Override
    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v2
    }

    protected List<ExtensionModule> getModules2() throws ParsingException {
        return ListUtil.cast(getModules1());
    }

    @Override
    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v2
    }

}
