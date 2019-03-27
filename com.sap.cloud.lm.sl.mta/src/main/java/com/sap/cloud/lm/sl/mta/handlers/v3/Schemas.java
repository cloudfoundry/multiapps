package com.sap.cloud.lm.sl.mta.handlers.v3;

import com.sap.cloud.lm.sl.mta.schema.ListElement;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class Schemas extends com.sap.cloud.lm.sl.mta.handlers.v2.Schemas {

    public static final MapElement MTAD = new MapElement();
    public static final MapElement MODULE = new MapElement();
    public static final MapElement REQUIRED_DEPENDENCY = new MapElement();
    public static final MapElement RESOURCE_REQUIRED_DEPENDENCY = new MapElement();
    public static final MapElement PROVIDED_DEPENDENCY = new MapElement();
    public static final MapElement RESOURCE = new MapElement();

    public static final MapElement MTAEXT = new MapElement();
    public static final MapElement EXT_MODULE = new MapElement();
    public static final MapElement EXT_REQUIRED_DEPENDENCY = new MapElement();
    public static final MapElement EXT_PROVIDED_DEPENDENCY = new MapElement();
    public static final MapElement EXT_RESOURCE = new MapElement();

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
        MTAD.add("parameters-metadata", PROPERTIES);

        MODULE.add("name", UNIQUE_MTA_IDENTIFIER);
        MODULE.add("type", STRING_REQUIRED);
        MODULE.add("description", STRING);
        MODULE.add("path", STRING);
        MODULE.add("properties", PROPERTIES);
        MODULE.add("properties-metadata", PROPERTIES);
        MODULE.add("parameters", PROPERTIES);
        MODULE.add("parameters-metadata", PROPERTIES);
        MODULE.add("requires", new ListElement(REQUIRED_DEPENDENCY));
        MODULE.add("provides", new ListElement(PROVIDED_DEPENDENCY));
        MODULE.add("deployed-after", new ListElement(STRING));

        REQUIRED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        REQUIRED_DEPENDENCY.add("group", STRING);
        REQUIRED_DEPENDENCY.add("list", STRING);
        REQUIRED_DEPENDENCY.add("properties", PROPERTIES);
        REQUIRED_DEPENDENCY.add("parameters", PROPERTIES);
        REQUIRED_DEPENDENCY.add("properties-metadata", PROPERTIES);
        REQUIRED_DEPENDENCY.add("parameters-metadata", PROPERTIES);

        RESOURCE_REQUIRED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE_REQUIRED_DEPENDENCY.add("properties", PROPERTIES);
        RESOURCE_REQUIRED_DEPENDENCY.add("parameters", PROPERTIES);
        RESOURCE_REQUIRED_DEPENDENCY.add("properties-metadata", PROPERTIES);
        RESOURCE_REQUIRED_DEPENDENCY.add("parameters-metadata", PROPERTIES);

        PROVIDED_DEPENDENCY.add("name", UNIQUE_MTA_IDENTIFIER);
        PROVIDED_DEPENDENCY.add("public", BOOLEAN);
        PROVIDED_DEPENDENCY.add("properties", PROPERTIES);
        PROVIDED_DEPENDENCY.add("properties-metadata", PROPERTIES);
        PROVIDED_DEPENDENCY.add("parameters", PROPERTIES);
        PROVIDED_DEPENDENCY.add("parameters-metadata", PROPERTIES);

        RESOURCE.add("name", UNIQUE_MTA_IDENTIFIER);
        RESOURCE.add("type", STRING);
        RESOURCE.add("active", BOOLEAN);
        RESOURCE.add("description", STRING);
        RESOURCE.add("optional", BOOLEAN);
        RESOURCE.add("properties", PROPERTIES);
        RESOURCE.add("parameters", PROPERTIES);
        RESOURCE.add("properties-metadata", PROPERTIES);
        RESOURCE.add("parameters-metadata", PROPERTIES);
        RESOURCE.add("requires", new ListElement(RESOURCE_REQUIRED_DEPENDENCY));

        MTAEXT.add("_schema-version", OBJECT_REQUIRED);
        MTAEXT.add("ID", NON_UNIQUE_MTA_IDENTIFIER);
        MTAEXT.add("description", STRING);
        MTAEXT.add("extends", STRING_REQUIRED);
        MTAEXT.add("provider", STRING);
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
        EXT_RESOURCE.add("active", BOOLEAN);
        EXT_RESOURCE.add("properties", PROPERTIES);
        EXT_RESOURCE.add("parameters", PROPERTIES);
        EXT_RESOURCE.add("requires", new ListElement(EXT_REQUIRED_DEPENDENCY));
    }

}
