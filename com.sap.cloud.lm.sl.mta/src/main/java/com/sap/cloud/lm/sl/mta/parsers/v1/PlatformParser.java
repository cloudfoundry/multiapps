package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.PLATFORM_TYPE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1.Platform.Builder;
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

    protected final Set<String> usedModuleTypeNames = new HashSet<String>();
    protected final Set<String> usedResourceTypeNames = new HashSet<String>();

    public PlatformParser(Map<String, Object> source) {
        this(PLATFORM_TYPE, source);
    }

    protected PlatformParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public Platform parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDescription(getDescription());
        builder.setVersion(getVersion());
        builder.setProperties(getProperties());
        builder.setModuleTypes1(getModuleTypes1());
        builder.setResourceTypes1(getResourceTypes1());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected String getVersion() throws ParsingException {
        return getStringElement(VERSION);
    }

    protected String getDescription() throws ParsingException {
        return getStringElement(DESCRIPTION);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected List<PlatformModuleType> getModuleTypes1() throws ParsingException {
        return getListElement(MODULE_TYPES, new ListParser<PlatformModuleType>() {
            @Override
            protected PlatformModuleType parseItem(Map<String, Object> map) throws ParsingException {
                return getModuleTypeParser(map).setUsedValues(usedModuleTypeNames)
                    .parse();
            }
        });
    }

    protected ModuleTypeParser getModuleTypeParser(Map<String, Object> source) {
        return new ModuleTypeParser(source);
    }

    protected List<PlatformResourceType> getResourceTypes1() throws ParsingException {
        return getListElement(RESOURCE_TYPES, new ListParser<PlatformResourceType>() {
            @Override
            protected PlatformResourceType parseItem(Map<String, Object> map) throws ParsingException {
                return getResourceTypeParser(map).setUsedValues(usedResourceTypeNames)
                    .parse();
            }
        });
    }

    protected ResourceTypeParser getResourceTypeParser(Map<String, Object> source) {
        return new ResourceTypeParser(source);
    }

}
