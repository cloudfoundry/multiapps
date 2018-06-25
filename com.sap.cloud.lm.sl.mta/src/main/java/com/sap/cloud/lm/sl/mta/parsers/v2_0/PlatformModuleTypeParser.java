package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.PTF_MODULE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2_0.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.TargetModuleType.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformModuleTypeParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.PlatformModuleTypeParser {

    public static final String PARAMETERS = "parameters";

    public PlatformModuleTypeParser(Map<String, Object> source) {
        super(PTF_MODULE_TYPE, source);
    }

    protected PlatformModuleTypeParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public TargetModuleType parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
