package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.PLATFORM;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v3_0.TargetResourceType;
import com.sap.cloud.lm.sl.mta.model.v3_0.Target;
import com.sap.cloud.lm.sl.mta.model.v3_0.Target.TargetBuilder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class TargetParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.TargetParser {

    public TargetParser(Map<String, Object> source) {
        super(PLATFORM, source);
    }

    protected TargetParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Target parse() throws ParsingException {
        TargetBuilder builder = new TargetBuilder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setType(getType());
        builder.setModuleTypes3_0(getModuleTypes3_0());
        builder.setResourceTypes3_0(getResourceTypes3_0());
        builder.setParameters(getParameters());
        return builder.build();
    }

    @Override
    protected PlatformModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new PlatformModuleTypeParser(source);
    }

    @Override
    protected PlatformResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new PlatformResourceTypeParser(source);
    }

    protected List<TargetModuleType> getModuleTypes3_0() throws ParsingException {
        return ListUtil.cast(getModuleTypes2_0());
    }

    protected List<TargetResourceType> getResourceTypes3_0() throws ParsingException {
        return ListUtil.cast(getResourceTypes2_0());
    }

}
