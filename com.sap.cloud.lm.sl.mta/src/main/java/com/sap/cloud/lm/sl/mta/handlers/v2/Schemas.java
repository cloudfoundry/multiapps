package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.schema.Element;
import com.sap.cloud.lm.sl.mta.schema.ListElement;
import com.sap.cloud.lm.sl.mta.schema.MapElement;
import com.sap.cloud.lm.sl.mta.schema.Element.ElementBuilder;

public class Schemas {

    public static final String MTA_IDENTIFIER_PATTERN = "^[A-Za-z0-9_\\-\\.]+$";
    public static final int MTA_IDENTIFIER_MAX_LENGTH = 128;

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

    public static final MapElement PLATFORM = new MapElement();
    public static final MapElement MODULE_TYPE = new MapElement();
    public static final MapElement RESOURCE_TYPE = new MapElement();

    public static final Element OBJECT_REQUIRED = new ElementBuilder().type(Object.class)
        .required(true)
        .buildSimple();
    public static final Element NON_UNIQUE_MTA_IDENTIFIER = new ElementBuilder().required(true)
        .pattern(MTA_IDENTIFIER_PATTERN)
        .maxLength(MTA_IDENTIFIER_MAX_LENGTH)
        .buildSimple();
    public static final Element STRING_REQUIRED = new ElementBuilder().required(true)
        .buildSimple();
    public static final Element STRING = new ElementBuilder().buildSimple();
    public static final Element UNIQUE_MTA_IDENTIFIER = new ElementBuilder().required(true)
        .unique(true)
        .pattern(MTA_IDENTIFIER_PATTERN)
        .maxLength(MTA_IDENTIFIER_MAX_LENGTH)
        .buildSimple();
    public static final Element PROPERTIES = new ElementBuilder().type(Map.class)
        .buildSimple();
    public static final Element BOOLEAN = new ElementBuilder().type(Boolean.class)
        .buildSimple();
    public static final ListElement LIST = new ListElement(STRING);

    static {
        MTAD.add("_schema-version", OBJECT_REQUIRED);
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
        PROVIDED_DEPENDENCY.add("parameters", PROPERTIES);

        RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE.add("type", STRING);
        RESOURCE.add("description", STRING);
        RESOURCE.add("properties", PROPERTIES);
        RESOURCE.add("parameters", PROPERTIES);

        MTAEXT.add("_schema-version", OBJECT_REQUIRED);
        MTAEXT.add("ID", NON_UNIQUE_MTA_IDENTIFIER);
        MTAEXT.add("ext_description", STRING);
        MTAEXT.add("description", STRING);
        MTAEXT.add("extends", STRING_REQUIRED);
        MTAEXT.add("ext_provider", STRING);
        MTAEXT.add("provider", STRING);
        MTAEXT.add("targets", LIST);
        MTAEXT.add("target-platforms", LIST);
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

        PLATFORM.add("name", UNIQUE_MTA_IDENTIFIER);
        PLATFORM.add("parameters", PROPERTIES);
        PLATFORM.add("module-types", new ListElement(MODULE_TYPE));
        PLATFORM.add("resource-types", new ListElement(RESOURCE_TYPE));

        MODULE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE_TYPE.add("properties", PROPERTIES);
        MODULE_TYPE.add("parameters", PROPERTIES);

        RESOURCE_TYPE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE_TYPE.add("parameters", PROPERTIES);
    }

}
