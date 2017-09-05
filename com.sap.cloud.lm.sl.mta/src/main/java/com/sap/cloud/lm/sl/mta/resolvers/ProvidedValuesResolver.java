package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.Map;

public interface ProvidedValuesResolver<E extends Exception> {

    Map<String, Object> resolveProvidedValues(String providesName) throws E;

}
