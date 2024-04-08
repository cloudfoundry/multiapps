package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;

public class LiveRoutesProvidedParametersResolver implements Resolver<DeploymentDescriptor> {

    private static final String ROUTES_KEY_NAME = "routes";
    private static final String ROUTES_PLACEHOLDER_PREFIX = "{routes/";
    private static final String ROUTE_KEY_NAME = "route";
    private static final String DASH_SYMBOL = "/";
    private final DeploymentDescriptor descriptor;
    private final String useLiveRoutesParameterKeyName;

    public LiveRoutesProvidedParametersResolver(DeploymentDescriptor descriptor, String useLiveRoutesParameterKeyName) {
        this.descriptor = descriptor;
        this.useLiveRoutesParameterKeyName = useLiveRoutesParameterKeyName;
    }

    @Override
    public DeploymentDescriptor resolve() {
        descriptor.getModules()
                  .stream()
                  .filter(module -> module.getParameters()
                                          .containsKey(ROUTES_KEY_NAME))
                  .forEach(this::resolveProvidedDependencyOfModule);

        return descriptor;
    }

    private void resolveProvidedDependencyOfModule(Module module) {
        module.getProvidedDependencies()
              .stream()
              .filter(this::shouldUseLiveRoutes)
              .forEach(providedDependency -> resolveProvidedDependencyParameters(providedDependency, module));
    }

    private boolean shouldUseLiveRoutes(ProvidedDependency providedDependency) {
        if (providedDependency.getParameters()
                              .containsKey(useLiveRoutesParameterKeyName)) {
            return MiscUtil.<Boolean> cast(providedDependency.getParameters()
                                                             .get(useLiveRoutesParameterKeyName));
        }
        return false;
    }

    private void resolveProvidedDependencyParameters(ProvidedDependency providedDependency, Module module) {
        for (Map.Entry<String, Object> property : providedDependency.getProperties()
                                                                    .entrySet()) {
            if (!(property.getValue() instanceof String)) {
                continue;
            }

            List<Reference> matchedReferences = ReferencePattern.PLACEHOLDER.match(MiscUtil.cast(property.getValue()));

            for (Reference reference : matchedReferences) {
                handlePropertyReferences(module, property, MiscUtil.cast(property.getValue()), reference);
            }
        }
    }

    private void handlePropertyReferences(Module module, Map.Entry<String, Object> property, String propertyValueString,
                                          Reference reference) {
        if (!reference.getMatchedPattern()
                      .contains(ROUTES_PLACEHOLDER_PREFIX)) {
            return;
        }

        int routeIndex = getRouteIndex(reference.getKey());

        List<Map<String, Object>> routes = MiscUtil.cast(module.getParameters()
                                                               .get(ROUTES_KEY_NAME));

        String route = (String) routes.get(routeIndex)
                                      .get(ROUTE_KEY_NAME);
        String newPropertyValue = propertyValueString.replace(reference.getMatchedPattern(), route);

        property.setValue(newPropertyValue);
    }

    private int getRouteIndex(String propertyValueString) {
        int indexOfSlash = propertyValueString.indexOf(DASH_SYMBOL);
        int indexOfSecondSlash = propertyValueString.indexOf(DASH_SYMBOL, indexOfSlash + 1);
        String routeIndex = propertyValueString.substring(indexOfSlash + 1, indexOfSecondSlash);

        return Integer.parseInt(routeIndex);
    }
}
