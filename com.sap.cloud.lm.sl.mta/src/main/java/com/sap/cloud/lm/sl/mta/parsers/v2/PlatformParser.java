package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.PLATFORM;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2.Platform.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformParser extends com.sap.cloud.lm.sl.mta.parsers.v1.PlatformParser {

    public static final String PARAMETERS = "parameters";

    public PlatformParser(Map<String, Object> source) {
        super(PLATFORM, source);
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
        builder.setModuleTypes2(getModuleTypes2());
        builder.setResourceTypes2(getResourceTypes2());
        return builder.build();
    }

    protected List<ModuleType> getModuleTypes2() {
        return ListUtil.cast(getModuleTypes1());
    }

    @Override
    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<ResourceType> getResourceTypes2() {
        return ListUtil.cast(getResourceTypes1());
    }

    @Override
    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
