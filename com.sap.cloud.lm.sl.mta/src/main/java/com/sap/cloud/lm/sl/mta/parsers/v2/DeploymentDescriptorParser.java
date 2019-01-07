package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.MTAD;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class DeploymentDescriptorParser extends ModelParser<DeploymentDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA deployment descriptor";

    public static final String PARAMETERS = "parameters";
    public static final String ID = "ID";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String PROVIDER = "provider";
    public static final String COPYRIGHT = "copyright";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PROPERTIES = "properties";
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
        Builder builder = new Builder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setVersion(getVersion());
        builder.setProvider(getProvider());
        builder.setCopyright(getCopyright());
        builder.setModules2(getModules2());
        builder.setResources2(getResources2());
        builder.setParameters(getParameters());
        builder.setSchemaVersion(getSchemaVersion());
        return builder.build();
    }

    protected List<Module> getModules() {
        return getListElement(MODULES, new ListParser<Module>() {
            @Override
            protected Module parseItem(Map<String, Object> map) {
                return (Module) getModuleParser(map).setUsedProvidedDependencyNames(usedDependencyNames)
                    .setUsedValues(usedModuleNames)
                    .parse();
            }
        });
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
    
    protected String getSchemaVersion() {
        return getSchemaVersion(SCHEMA_VERSION);
    }

    protected String getId() {
        return getStringElement(ID);
    }

    protected String getDescription() {
        return getStringElement(DESCRIPTION);
    }

    protected String getVersion() {
        return getStringElement(VERSION);
    }

    protected String getProvider() {
        return getStringElement(PROVIDER);
    }

    protected String getCopyright() {
        return getStringElement(COPYRIGHT);
    }

    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source);
    }

    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }
    

    protected List<Module> getModules2() {
        return ListUtil.cast(getModules());
    }

    protected List<Resource> getResources2() {
        return ListUtil.cast(getResources());
    }
    
}
