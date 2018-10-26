package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.PLATFORM;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.Target;
import com.sap.cloud.lm.sl.mta.model.v2.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.TargetResourceType;
import com.sap.cloud.lm.sl.mta.model.v2.Target.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class TargetParser extends com.sap.cloud.lm.sl.mta.parsers.v1.TargetParser {

    public static final String PARAMETERS = "parameters";

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
        builder.setModuleTypes2(getModuleTypes2());
        builder.setResourceTypes2(getResourceTypes2());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    @Override
    protected PlatformModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new PlatformModuleTypeParser(source);
    }

    @Override
    protected PlatformResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new PlatformResourceTypeParser(source);
    }

    protected List<TargetModuleType> getModuleTypes2() {
        return ListUtil.cast(getModuleTypes1());
    }

    protected List<TargetResourceType> getResourceTypes2() {
        return ListUtil.cast(getResourceTypes1());
    }

}
