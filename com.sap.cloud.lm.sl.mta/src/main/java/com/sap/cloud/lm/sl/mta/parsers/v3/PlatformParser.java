package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.PLATFORM_TYPE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.Platform;
import com.sap.cloud.lm.sl.mta.model.v3.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v3.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v3.Platform.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformParser extends com.sap.cloud.lm.sl.mta.parsers.v2.PlatformParser {

    public PlatformParser(Map<String, Object> source) {
        super(PLATFORM_TYPE, source);
    }

    protected PlatformParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Platform parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setParameters(getParameters());
        builder.setModuleTypes3(getModuleTypes3());
        builder.setResourceTypes3(getResourceTypes3());
        return builder.build();
    }

    protected List<PlatformModuleType> getModuleTypes3() throws ParsingException {
        return ListUtil.cast(getModuleTypes2());
    }

    @Override
    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<PlatformResourceType> getResourceTypes3() throws ParsingException {
        return ListUtil.cast(getResourceTypes2());
    }

    @Override
    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

}
