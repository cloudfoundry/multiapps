package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.PLATFORM;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2.Platform.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class PlatformParser extends ModelParser<Platform> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA platform type";

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String MODULE_TYPES = "module-types";
    public static final String PROPERTIES = "properties";
    public static final String RESOURCE_TYPES = "resource-types";
    public static final String PARAMETERS = "parameters";

    protected final Set<String> usedModuleTypeNames = new HashSet<>();
    protected final Set<String> usedResourceTypeNames = new HashSet<>();

    public PlatformParser(Map<String, Object> source) {
        this(PLATFORM, source);
    }

    protected PlatformParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
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

    protected String getName() {
        return getStringElement(NAME);
    }

    protected String getVersion() {
        return getStringElement(VERSION);
    }

    protected String getDescription() {
        return getStringElement(DESCRIPTION);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected List<ModuleType> getModuleTypes2() {
        return getListElement(MODULE_TYPES, new ListParser<ModuleType>() {
            @Override
            protected ModuleType parseItem(Map<String, Object> map) {
                return getModuleTypeParser(map).setUsedValues(usedModuleTypeNames)
                    .parse();
            }
        });
    }

    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<ResourceType> getResourceTypes2() {
        return getListElement(RESOURCE_TYPES, new ListParser<ResourceType>() {
            @Override
            protected ResourceType parseItem(Map<String, Object> map) {
                return getResourceTypeParser(map).setUsedValues(usedResourceTypeNames)
                    .parse();
            }
        });
    }

    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
