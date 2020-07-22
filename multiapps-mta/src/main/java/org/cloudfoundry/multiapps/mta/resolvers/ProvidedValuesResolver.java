package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Map;

public interface ProvidedValuesResolver {

    Map<String, Object> resolveProvidedValues(String providesName);

}
