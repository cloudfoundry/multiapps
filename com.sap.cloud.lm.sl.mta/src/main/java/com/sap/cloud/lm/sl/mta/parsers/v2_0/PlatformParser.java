package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.PLATFORM_TYPE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform.PlatformBuilder;
import com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.PlatformParser {

    public static final String PARAMETERS = "parameters";

    public PlatformParser(Map<String, Object> source) {
        super(PLATFORM_TYPE, source);
    }

    protected PlatformParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Platform parse() throws ParsingException {
        PlatformBuilder builder = new PlatformBuilder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setParameters(getParameters());
        builder.setModuleTypes2_0(getModuleTypes2_0());
        builder.setResourceTypes2_0(getResourceTypes2_0());
        return builder.build();
    }

    protected List<ModuleType> getModuleTypes2_0() throws ParsingException {
        return ListUtil.cast(getModuleTypes1_0());
    }

    @Override
    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<ResourceType> getResourceTypes2_0() throws ParsingException {
        return ListUtil.cast(getResourceTypes1_0());
    }

    @Override
    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
