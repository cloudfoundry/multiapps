package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.MTAEXT;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor.Builder;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionDescriptorParser extends ModelParser<ExtensionDescriptor> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension descriptor";
    public static final String ID = "ID";
    public static final String EXT_DESCRIPTION = "ext_description";
    public static final String EXTENDS = "extends";
    public static final String EXT_PROVIDER = "ext_provider";
    public static final String DESCRIPTION = "description";
    public static final String PROVIDER = "provider";
    public static final String TARGET_PLATFORMS = "target-platforms";
    public static final String MODULES = "modules";
    public static final String RESOURCES = "resources";
    public static final String PROPERTIES = "properties";
    public static final String SCHEMA_VERSION = "_schema-version";
    public static final String PARAMETERS = "parameters";
    public static final String DEPLOY_TARGETS = "targets";

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
        Builder builder = new Builder();
        builder.setId(getId());
        builder.setDescription(getDescription());
        builder.setParentId(getParentId());
        builder.setProvider(getProvider());
        builder.setSchemaVersion(getSchemaVersion());
        // "target-platforms" parameter is kept in version 2.* for backwards compatibility and removed in versions 3.*
        builder.setDeployTargets(getTargetPLatforms());
        if (getDeployTargets() != null) {
            builder.setDeployTargets(getDeployTargets());
        }
        builder.setModules2(getModules2());
        builder.setResources2(getResources2());
        builder.setParameters(getParameters());
        return builder.build();
    }

    /**
     * @deprecated Use {@link #getDescription()} instead.
     */
    @Deprecated
    protected String getExtensionDescription() {
        return getDescription();
    }

    /**
     * @deprecated Use {@link #getProvider()} instead.
     */
    @Deprecated
    protected String getExtensionProvider() {
        return getProvider();
    }

    protected String getDescription() {
        String description = getStringElement(DESCRIPTION);
        if (description != null) {
            return description;
        }
        return getStringElement(EXT_DESCRIPTION);
    }

    protected String getProvider() {
        String provider = getStringElement(PROVIDER);
        if (provider != null) {
            return provider;
        }
        return getStringElement(EXT_PROVIDER);
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

    protected List<String> getTargetPLatforms() {
        return getListElement(TARGET_PLATFORMS);
    }

    protected List<ExtensionModule> getModules2() {
        return ListUtil.cast(getModules());
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

    protected List<String> getDeployTargets() {
        return getListElement(DEPLOY_TARGETS);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionResource> getResources2() {
        return ListUtil.cast(getResources());
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
