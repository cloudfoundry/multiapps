package com.sap.cloud.lm.sl.mta.parsers.v1_0;

import static com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas.MTAD;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class DeploymentDescriptorParser extends ModelParser<DeploymentDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA deployment descriptor";

    public static final String ID = "ID";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String PROVIDER = "provider";
    public static final String COPYRIGHT = "copyright";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PROPERTIES = "properties";
    public static final String SCHEMA_VERSION = "_schema-version";

    protected final Set<String> usedModuleNames = new HashSet<String>();
    protected final Set<String> usedDependencyNames = new HashSet<String>();

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
        builder.setSchemaVersion(getSchemaVersion());
        builder.setModules1_0(getModules1_0());
        builder.setProperties(getProperties());
        builder.setResources1_0(getResources1_0());
        return builder.build();
    }

    protected String getId() throws ParsingException {
        return getStringElement(ID);
    }

    protected String getDescription() throws ParsingException {
        return getStringElement(DESCRIPTION);
    }

    protected String getVersion() throws ParsingException {
        return getStringElement(VERSION);
    }

    protected String getProvider() throws ParsingException {
        return getStringElement(PROVIDER);
    }

    protected String getCopyright() throws ParsingException {
        return getStringElement(COPYRIGHT);
    }

    protected String getSchemaVersion() throws ParsingException {
        return getSchemaVersion(SCHEMA_VERSION);
    }

    protected List<Module> getModules1_0() throws ParsingException {
        return getListElement(MODULES, new ListParser<Module>() {
            @Override
            protected Module parseItem(Map<String, Object> map) throws ParsingException {
                return getModuleParser(map).setUsedProvidedDependencyNames(usedDependencyNames)
                    .setUsedValues(usedModuleNames)
                    .parse();
            }
        });
    }

    protected ModuleParser getModuleParser(Map<String, Object> source) {
        return new ModuleParser(source);
    }

    protected List<Resource> getResources1_0() throws ParsingException {
        return getListElement(RESOURCES, new ListParser<Resource>() {
            @Override
            protected Resource parseItem(Map<String, Object> map) throws ParsingException {
                return getResourceParser(map).setUsedValues(usedDependencyNames)
                    .parse();
            }
        });
    }

    protected ResourceParser getResourceParser(Map<String, Object> source) {
        return new ResourceParser(source);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
