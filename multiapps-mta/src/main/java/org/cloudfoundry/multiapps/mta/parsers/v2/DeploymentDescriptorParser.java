package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.MTAD;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class DeploymentDescriptorParser extends ModelParser<DeploymentDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA deployment descriptor";

    public static final String ID = "ID";
    public static final String VERSION = "version";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PARAMETERS = "parameters";
    public static final String SCHEMA_VERSION = "_schema-version";

    protected final Set<String> usedModuleNames = new HashSet<>();
    protected final Set<String> usedDependencyNames = new HashSet<>();

    public DeploymentDescriptorParser(Map<String, Object> source) {
        this(MTAD, source);
    }

    protected DeploymentDescriptorParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public DeploymentDescriptor parse() throws ParsingException {
        return createEntity().setSchemaVersion(getSchemaVersion())
                             .setId(getId())
                             .setVersion(getVersion())
                             .setModules(getModules())
                             .setResources(getResources())
                             .setParameters(getParameters());
    }

    protected DeploymentDescriptor createEntity() {
        return DeploymentDescriptor.createV2();
    }

    protected String getSchemaVersion() {
        return getSchemaVersion(SCHEMA_VERSION);
    }

    protected String getId() {
        return getStringElement(ID);
    }

    protected String getVersion() {
        return getStringElement(VERSION);
    }

    protected List<Module> getModules() {
        return getListElement(MODULES, new ListParser<Module>() {
            @Override
            protected Module parseItem(Map<String, Object> map) {
                return getModuleParser(map).setUsedProvidedDependencyNames(usedDependencyNames)
                                           .setUsedValues(usedModuleNames)
                                           .parse();
            }
        });
    }

    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source);
    }

    protected List<Resource> getResources() {
        return getListElement(RESOURCES, new ListParser<Resource>() {
            @Override
            protected Resource parseItem(Map<String, Object> map) {
                return getResourceParser(map).setUsedValues(usedDependencyNames)
                                             .parse();
            }
        });
    }

    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}
