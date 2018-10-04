package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.MODULE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleTypeParser extends ModelParser<PlatformModuleType> {

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
    public PlatformModuleType parse() throws ParsingException {
        Builder builder = new Builder();
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
