package com.sap.cloud.lm.sl.mta.parsers.v1_0;

import static com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas.MODULE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType.ModuleTypeBuilder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleTypeParser extends ModelParser<ModuleType> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA module type";

    public static final String PROPERTIES = "properties";
    public static final String NAME = "name";
    public static final String DEPLOYER = "deployer";

    public ModuleTypeParser(Map<String, Object> source) {
        this(MODULE_TYPE, source);
    }

    protected ModuleTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ModuleType parse() throws ParsingException {
        ModuleTypeBuilder builder = new ModuleTypeBuilder();
        builder.setName(getName());
        builder.setDeployer(getDeployer());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected String getDeployer() throws ParsingException {
        return getStringElement(DEPLOYER);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

}
