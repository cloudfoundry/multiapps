package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.multiapps.mta.model.*;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.Reference;
import org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern;

public class ResourceLiveParameterResolver {

    private static final String CONFIG = "config";
    private static final String DEFAULT_URL = "${default-url}";
    private static final String DEFAULT_LIVE_URL = "default-live-url";
    private DeploymentDescriptor deploymentDescriptor;

    public ResourceLiveParameterResolver(DeploymentDescriptor deploymentDescriptor) {
        this.deploymentDescriptor = deploymentDescriptor;
    }

    public void resolveResource(Resource resource) {
        if (resource.getParameters()
                    .containsKey(CONFIG)) {
            Map<String, Object> serviceCreationParameters = castToMap(resource.getParameters()
                                                                              .get(CONFIG));
            serviceCreationParameters.entrySet()
                                     .forEach(entry -> resolveResourceParameter(entry, resource));
        }
    }

    private Map<String, Object> castToMap(Object entryValue) {
        return (Map<String, Object>) entryValue;
    }

    private void resolveResourceParameter(Map.Entry<String, Object> entry, Resource resource) {
        if (entry.getValue() instanceof Map) {
            resolveMapParameter(castToMap(entry.getValue()), resource);
        } else if (entry.getValue() instanceof String) {
            resolveConfigParameter(entry, resource);
        }
    }

    private void resolveMapParameter(Map<String, Object> resourceParameter, Resource resource) {
        resourceParameter.entrySet()
                         .forEach(entry -> resolveResourceParameter(entry, resource));
    }

    private void resolveConfigParameter(Map.Entry<String, Object> resourceParameter, Resource resource) {
        String resourceParameterValue = (String) resourceParameter.getValue();
        List<Reference> matchedReferences = ReferencePattern.FULLY_QUALIFIED.match(resourceParameterValue);

        matchedReferences.forEach(ref -> updateParamsFromReference(resource, ref, resourceParameterValue, resourceParameter));
    }

    private void updateParamsFromReference(Resource resource, Reference ref, String resourceParameterValue,
                                           Map.Entry<String, Object> resourceParameter) {
        deploymentDescriptor.getModules()
                            .stream()
                            .filter(module -> isModuleNeeded(module, ref.getDependencyName()))
                            .findFirst()
                            .ifPresent(module -> updateMapFromModule(resource, ref, module, resourceParameterValue, resourceParameter));
    }

    private void updateMapFromModule(Resource resource, Reference ref, Module module, String resourceParameterValue,
                                     Map.Entry<String, Object> resourceParameter) {
        Optional<ProvidedDependency> moduleProvidedDependencyOpt = module.getProvidedDependencies()
                                                                         .stream()
                                                                         .filter(dp -> dp.getName()
                                                                                         .equals(ref.getDependencyName()))
                                                                         .findAny();
        Optional<RequiredDependency> resourceRequiredDependencyOpt = resource.getRequiredDependencies()
                                                                             .stream()
                                                                             .filter(reqDp -> reqDp.getName()
                                                                                                   .equals(ref.getDependencyName()))
                                                                             .findAny();

        if (moduleProvidedDependencyOpt.isPresent() && resourceRequiredDependencyOpt.isPresent()) {
            Object requiredDependencyValue = moduleProvidedDependencyOpt.get()
                                                                        .getProperties()
                                                                        .get(ref.getKey());

            if (requiredDependencyValue instanceof Map) {
                resolveModuleProvidedDependency(castToMap(requiredDependencyValue), module);
                resourceParameter.setValue(requiredDependencyValue);
            } else if (isStringValueReplaceable(requiredDependencyValue)) {
                replaceValueInResourceParameter(module, resourceParameterValue, resourceParameter, ref,
                                                castToString(requiredDependencyValue));
            }
        }
    }

    private boolean isStringValueReplaceable(Object object) {
        return object instanceof String && castToString(object).contains(DEFAULT_URL);
    }

    private void replaceValueInResourceParameter(Module module, String resourceParameterValue, Map.Entry<String, Object> resourceParameter,
                                                 Reference reference, String requiredDependencyValueString) {
        requiredDependencyValueString = requiredDependencyValueString.replace(DEFAULT_URL, castToString(module.getParameters()
                                                                                                              .get(DEFAULT_LIVE_URL)));
        requiredDependencyValueString = resourceParameterValue.replace(reference.getMatchedPattern(), requiredDependencyValueString);

        resourceParameter.setValue(requiredDependencyValueString);
    }

    private void resolveModuleProvidedDependency(Map<String, Object> requiredDependencyMap, Module module) {
        requiredDependencyMap.entrySet()
                             .forEach(entry -> updateModuleDependencyFromMap(module, entry));
    }

    private void updateModuleDependencyFromMap(Module module, Map.Entry<String, Object> entry) {
        if (isStringValueReplaceable(entry.getValue())) {
            entry.setValue(castToString(entry.getValue()).replace(DEFAULT_URL, castToString(module.getParameters()
                                                                                                  .get(DEFAULT_LIVE_URL))));
        } else if (entry.getValue() instanceof Map) {
            resolveModuleProvidedDependency(castToMap(entry.getValue()), module);
        }
    }

    private boolean isModuleNeeded(Module module, String name) {
        return module.getProvidedDependencies()
                     .stream()
                     .anyMatch(dependency -> dependency.getName()
                                                       .equals(name));
    }

    private String castToString(Object object) {
        return String.valueOf(object);
    }

}
