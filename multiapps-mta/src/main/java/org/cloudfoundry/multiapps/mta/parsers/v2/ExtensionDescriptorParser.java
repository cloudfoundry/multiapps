package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.MTAEXT;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionDescriptorParser extends ModelParser<ExtensionDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension descriptor";
    public static final String SCHEMA_VERSION = "_schema-version";
    public static final String ID = "ID";
    public static final String EXTENDS = "extends";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";

    protected final Set<String> usedModuleNames = new HashSet<>();
    protected final Set<String> usedProvidedDependencyNames = new HashSet<>();
    protected final Set<String> usedResourceNames = new HashSet<>();

    public ExtensionDescriptorParser(Map<String, Object> source) {
        this(MTAEXT, source);
    }

    protected ExtensionDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionDescriptor parse() throws ParsingException {
        return createEntity().setSchemaVersion(getSchemaVersion())
                             .setId(getId())
                             .setParentId(getParentId())
                             .setModules(getModules())
                             .setResources(getResources())
                             .setParameters(getParameters());
    }

    protected ExtensionDescriptor createEntity() {
        return ExtensionDescriptor.createV2();
    }

    protected String getSchemaVersion() {
        return getSchemaVersion(SCHEMA_VERSION);
    }

    protected String getId() {
        return getStringElement(ID);
    }

    protected String getParentId() {
        return getStringElement(EXTENDS);
    }

    protected List<ExtensionModule> getModules() {
        return getListElement(MODULES, new ListParser<ExtensionModule>() {
            @Override
            protected ExtensionModule parseItem(Map<String, Object> map) {
                return getModuleParser(map).setUsedProvidedDependencyNames(usedProvidedDependencyNames)
                                           .setUsedValues(usedModuleNames)
                                           .parse();
            }
        });
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionResource> getResources() {
        return getListElement(RESOURCES, new ListParser<ExtensionResource>() {
            @Override
            protected ExtensionResource parseItem(Map<String, Object> map) {
                return getResourceParser(map).setUsedValues(usedResourceNames)
                                             .parse();
            }
        });
    }

    protected ExtensionResourceParser getResourceParser(Map<String, Object> source) {
        return new ExtensionResourceParser(source); // v2
    }

    protected ExtensionModuleParser getModuleParser(Map<String, Object> source) {
        return new ExtensionModuleParser(source); // v2
    }

}
