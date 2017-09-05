package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.schema.Element;
import com.sap.cloud.lm.sl.mta.schema.Element.ElementBuilder;
import com.sap.cloud.lm.sl.mta.schema.ListElement;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class Schemas {

    public static final String MTA_IDENTIFIER_PATTERN = "^[A-Za-z0-9_\\-\\.]+$";
    public static final int MTA_IDENTIFIER_MAX_LENGTH = 128;

    public static final Element UNIQUE_MTA_IDENTIFIER = new ElementBuilder().required(true).unique(true).pattern(
        MTA_IDENTIFIER_PATTERN).maxLength(MTA_IDENTIFIER_MAX_LENGTH).buildSimple();
    public static final Element NON_UNIQUE_MTA_IDENTIFIER = new ElementBuilder().required(true).pattern(MTA_IDENTIFIER_PATTERN).maxLength(
        MTA_IDENTIFIER_MAX_LENGTH).buildSimple();
    public static final Element STRING = new ElementBuilder().buildSimple();
    public static final Element OBJECT = new ElementBuilder().type(Object.class).buildSimple();
    public static final Element STRING_REQUIRED = new ElementBuilder().required(true).buildSimple();
    public static final Element STRING_REQUIRED_UNIQUE = new ElementBuilder().required(true).unique(true).buildSimple();
    public static final Element PROPERTIES = new ElementBuilder().type(Map.class).buildSimple();
    public static final Element BOOLEAN = new ElementBuilder().type(Boolean.class).buildSimple();
    public static final ListElement LIST = new ListElement(STRING);

    public static final MapElement MTAD = new MapElement();
    public static final MapElement MODULE = new MapElement();
    public static final MapElement PROVIDED_DEPENDENCY = new MapElement();
    public static final MapElement RESOURCE = new MapElement();

    public static final MapElement MTAEXT = new MapElement();
    public static final MapElement EXT_MODULE = new MapElement();
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
        MTAD.add("properties", PROPERTIES);

        MODULE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE.add("type", STRING_REQUIRED);
        MODULE.add("description", STRING);
        MODULE.add("properties", PROPERTIES);
        MODULE.add("requires", LIST);
        MODULE.add("provides", new ListElement(PROVIDED_DEPENDENCY));

        PROVIDED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        PROVIDED_DEPENDENCY.add("groups", LIST);
        PROVIDED_DEPENDENCY.add("properties", PROPERTIES);

        RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE.add("type", STRING);
        RESOURCE.add("description", STRING);
        RESOURCE.add("groups", LIST);
        RESOURCE.add("properties", PROPERTIES);

        MTAEXT.add("_schema-version", OBJECT);
        MTAEXT.add("ID", NON_UNIQUE_MTA_IDENTIFIER);
        MTAEXT.add("ext_description", STRING);
        MTAEXT.add("extends", STRING_REQUIRED);
        MTAEXT.add("ext_provider", STRING);
        MTAEXT.add("target-platforms", LIST);
        MTAEXT.add("modules", new ListElement(EXT_MODULE));
        MTAEXT.add("resources", new ListElement(EXT_RESOURCE));
        MTAEXT.add("properties", PROPERTIES);

        EXT_MODULE.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_MODULE.add("properties", PROPERTIES);
        EXT_MODULE.add("provides", new ListElement(EXT_PROVIDED_DEPENDENCY));

        EXT_PROVIDED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_PROVIDED_DEPENDENCY.add("properties", PROPERTIES);

        EXT_RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        EXT_RESOURCE.add("type", STRING);
        EXT_RESOURCE.add("properties", PROPERTIES);

        PLATFORM_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PLATFORM_TYPE.add("version", STRING);
        PLATFORM_TYPE.add("description", STRING);
        PLATFORM_TYPE.add("properties", PROPERTIES);
        PLATFORM_TYPE.add("module-types", new ListElement(MODULE_TYPE));
        PLATFORM_TYPE.add("resource-types", new ListElement(RESOURCE_TYPE));

        MODULE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE_TYPE.add("deployer", STRING);
        MODULE_TYPE.add("properties", PROPERTIES);

        RESOURCE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE_TYPE.add("resource-manager", STRING);
        RESOURCE_TYPE.add("properties", PROPERTIES);

        PLATFORM.add("name", UNIQUE_MTA_IDENTIFIER);
        PLATFORM.add("type", STRING);
        PLATFORM.add("description", STRING);
        PLATFORM.add("properties", PROPERTIES);
        PLATFORM.add("module-types", new ListElement(PTF_MODULE_TYPE));
        PLATFORM.add("resource-types", new ListElement(PTF_RESOURCE_TYPE));

        PTF_MODULE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PTF_MODULE_TYPE.add("properties", PROPERTIES);

        PTF_RESOURCE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        PTF_RESOURCE_TYPE.add("properties", PROPERTIES);
    }

}
