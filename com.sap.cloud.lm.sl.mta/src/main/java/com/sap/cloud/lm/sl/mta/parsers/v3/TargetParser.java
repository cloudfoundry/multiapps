package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.PLATFORM;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.Target;
import com.sap.cloud.lm.sl.mta.model.v3.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v3.TargetResourceType;
import com.sap.cloud.lm.sl.mta.model.v3.Target.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class TargetParser extends com.sap.cloud.lm.sl.mta.parsers.v2.TargetParser {

    public TargetParser(Map<String, Object> source) {
        super(PLATFORM, source);
    }

    protected TargetParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Target parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setType(getType());
        builder.setModuleTypes3(getModuleTypes3());
        builder.setResourceTypes3(getResourceTypes3());
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

    protected List<TargetModuleType> getModuleTypes3() throws ParsingException {
        return ListUtil.cast(getModuleTypes2());
    }

    protected List<TargetResourceType> getResourceTypes3() throws ParsingException {
        return ListUtil.cast(getResourceTypes2());
    }

}
