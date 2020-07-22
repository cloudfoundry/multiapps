package org.cloudfoundry.multiapps.mta.parsers;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.PLATFORM;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ModuleType;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.ResourceType;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class PlatformParser extends ModelParser<Platform> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA platform type";

    public static final String NAME = "name";
    public static final String MODULE_TYPES = "module-types";
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
        return new Platform().setName(getName())
                             .setModuleTypes(getModuleTypes())
                             .setResourceTypes(getResourceTypes())
                             .setParameters(getParameters());
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected List<ModuleType> getModuleTypes() {
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

    protected List<ResourceType> getResourceTypes() {
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
