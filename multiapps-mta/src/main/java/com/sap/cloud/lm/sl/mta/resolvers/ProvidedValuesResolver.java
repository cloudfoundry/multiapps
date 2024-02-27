package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.Map;

public interface ProvidedValuesResolver {

    Map<String, Object> resolveProvidedValues(String providesName);

}
