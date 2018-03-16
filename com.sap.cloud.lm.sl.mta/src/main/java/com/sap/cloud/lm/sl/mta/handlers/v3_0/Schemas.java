package com.sap.cloud.lm.sl.mta.handlers.v3_0;

import com.sap.cloud.lm.sl.mta.schema.ListElement;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class Schemas extends com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas {

    public static final MapElement MTAD = new MapElement();
    public static final MapElement MODULE = new MapElement();
    public static final MapElement REQUIRED_DEPENDENCY = new MapElement();
    public static final MapElement PROVIDED_DEPENDENCY = new MapElement();
    public static final MapElement RESOURCE = new MapElement();

    public static final MapElement MTAEXT = new MapElement();
    public static final MapElement EXT_MODULE = new MapElement();
    public static final MapElement EXT_REQUIRED_DEPENDENCY = new MapElement();
    public static final MapElement EXT_PROVIDED_DEPENDENCY = new MapElement();
    public static final MapElement EXT_RESOURCE = new MapElement();

    public static final MapElement PLATFORM_TYPE = new MapElement();
    public static final MapElement MODULE_TYPE = new MapElement();
    public static final MapElement RESOURCE_TYPE = new MapElement();

    public static final MapElement PLATFORM = new MapElement();
    public static final MapElement PTF_MODULE_TYPE = new MapElement();
    public static final MapElement PTF_RESOURCE_TYPE = new MapElement();

    public static final ListElement PLATFORM_TYPES = new ListElement(PLATFORM_TYPE);
    public static final ListElement PLATFORMS = new ListElement(PLATFORM);

    static {
        MTAD.add("_schema-version", OBJECT);
        MTAD.add("ID", NON_UNIQUE_MTA_IDENTIFIER);
        MTAD.add("version", STRING_REQUIRED);
        MTAD.add("description", STRING);
        MTAD.add("provider", STRING);
        MTAD.add("copyright", STRING);
        MTAD.add("modules", new ListElement(MODULE));
        MTAD.add("resources", new ListElement(RESOURCE));
        MTAD.add("parameters", PROPERTIES);

        MODULE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE.add("type", STRING_REQUIRED);
        MODULE.add("description", STRING);
        MODULE.add("path", STRING);
        MODULE.add("properties", PROPERTIES);
        MODULE.add("parameters", PROPERTIES);
        MODULE.add("requires", new ListElement(REQUIRED_DEPENDENCY));
        MODULE.add("provides", new ListElement(PROVIDED_DEPENDENCY));

        REQUIRED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        REQUIRED_DEPENDENCY.add("group", STRING);
        REQUIRED_DEPENDENCY.add("list", STRING);
        REQUIRED_DEPENDENCY.add("properties", PROPERTIES);
        REQUIRED_DEPENDENCY.add("parameters", PROPERTIES);

        PROVIDED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        PROVIDED_DEPENDENCY.add("public", BOOLEAN);
        PROVIDED_DEPENDENCY.add("properties", PROPERTIES);

        RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE.add("type", STRING);
        RESOURCE.add("description", STRING);
        RESOURCE.add("properties", PROPERTIES);
        RESOURCE.add("parameters", PROPERTIES);

        MTAEXT.add("_schema-version", OBJECT);
        MTAEXT.add("ID", NON_UNIQUE_MTA_IDENTIFIER);
        MTAEXT.add("description", STRING);
        MTAEXT.add("extends", STRING_REQUIRED);
        MTAEXT.add("ext_provider", STRING);
        MTAEXT.add("targets", LIST);
        MTAEXT.add("modules", new ListElement(EXT_MODULE));
        MTAEXT.add("resources", new ListElement(EXT_RESOURCE));
        MTAEXT.add("parameters", PROPERTIES);

        EXT_MODULE.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_MODULE.add("properties", PROPERTIES);
        EXT_MODULE.add("parameters", PROPERTIES);
        EXT_MODULE.add("requires", new ListElement(EXT_REQUIRED_DEPENDENCY));
        EXT_MODULE.add("provides", new ListElement(EXT_PROVIDED_DEPENDENCY));

        EXT_PROVIDED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_PROVIDED_DEPENDENCY.add("properties", PROPERTIES);

        EXT_REQUIRED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_REQUIRED_DEPENDENCY.add("properties", PROPERTIES);
        EXT_REQUIRED_DEPENDENCY.add("parameters", PROPERTIES);

        EXT_RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_RESOURCE.add("properties", PROPERTIES);
        EXT_RESOURCE.add("parameters", PROPERTIES);

        PLATFORM_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PLATFORM_TYPE.add("description", STRING);
        PLATFORM_TYPE.add("parameters", PROPERTIES);
        PLATFORM_TYPE.add("module-types", new ListElement(MODULE_TYPE));
        PLATFORM_TYPE.add("resource-types", new ListElement(RESOURCE_TYPE));

        MODULE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE_TYPE.add("deployer", STRING);
        MODULE_TYPE.add("properties", PROPERTIES);
        MODULE_TYPE.add("parameters", PROPERTIES);

        RESOURCE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE_TYPE.add("resource-manager", STRING);
        RESOURCE_TYPE.add("parameters", PROPERTIES);

        PLATFORM.add("name", UNIQUE_MTA_IDENTIFIER);
        PLATFORM.add("type", STRING);
        PLATFORM.add("description", STRING);
        PLATFORM.add("parameters", PROPERTIES);
        PLATFORM.add("module-types", new ListElement(PTF_MODULE_TYPE));
        PLATFORM.add("resource-types", new ListElement(PTF_RESOURCE_TYPE));

        PTF_MODULE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PTF_MODULE_TYPE.add("properties", PROPERTIES);
        PTF_MODULE_TYPE.add("parameters", PROPERTIES);

        PTF_RESOURCE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PTF_RESOURCE_TYPE.add("parameters", PROPERTIES);
    }

}
