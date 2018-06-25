package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.PLATFORM_TYPE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v3_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v3_0.Platform.PlatformBuilder;
import com.sap.cloud.lm.sl.mta.model.v3_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.PlatformParser {

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
        builder.setModuleTypes3_0(getModuleTypes3_0());
        builder.setResourceTypes3_0(getResourceTypes3_0());
        return builder.build();
    }

    protected List<PlatformModuleType> getModuleTypes3_0() throws ParsingException {
        return ListUtil.cast(getModuleTypes2_0());
    }

    @Override
    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<PlatformResourceType> getResourceTypes3_0() throws ParsingException {
        return ListUtil.cast(getResourceTypes2_0());
    }

    @Override
    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

}
