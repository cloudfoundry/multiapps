package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.PROVIDED_DEPENDENCY;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ProvidedDependencyParser extends com.sap.cloud.lm.sl.mta.parsers.v1.ProvidedDependencyParser {

    public static final String PUBLIC = "public";

    public ProvidedDependencyParser(Map<String, Object> source) {
        super(PROVIDED_DEPENDENCY, source);
    }

    protected ProvidedDependencyParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ProvidedDependency parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setPublic(isPublic());
        builder.setProperties(getProperties());
        return builder.build();
    }

    protected Boolean isPublic() {
        return getBooleanElement(PUBLIC);
    }

}
