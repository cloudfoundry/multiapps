package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.Reference;
import org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern;

public class ResourceLiveParameterResolver {

    private static final String CONFIG = "config";
    private static final String DEFAULT_URL_PLACEHOLDER = "${default-url}";
    private static final String DEFAULT_URI_PLACEHOLDER = "${default-uri}";
    private static final String DEFAULT_HOST_PLACEHOLDER = "${default-host}";
    private static final String DEFAULT_LIVE_URL = "default-live-url";
    private static final String DEFAULT_LIVE_HOST = "default-live-host";
    private static final String DEFAULT_LIVE_URI = "default-live-uri";
    private static final Map<String, String> idleToLiveParameterPairs = Map.of(DEFAULT_URL_PLACEHOLDER, DEFAULT_LIVE_URL,
                                                                               DEFAULT_URI_PLACEHOLDER, DEFAULT_LIVE_URI,
                                                                               DEFAULT_HOST_PLACEHOLDER, DEFAULT_LIVE_HOST);
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

        if (moduleProvidedDependencyOpt.isPresent()) {
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
        return object instanceof String && idleToLiveParameterPairs.keySet()
                                                                   .stream()
                                                                   .anyMatch(defaultParamPlaceholder -> castToString(object).contains(defaultParamPlaceholder));
    }

    private void replaceValueInResourceParameter(Module module, String resourceParameterValue, Map.Entry<String, Object> resourceParameter,
                                                 Reference reference, String requiredDependencyValueString) {
        Map.Entry<String, String> requiredIdleToLiveParameterPair = getMapEntryOnCondition(requiredDependencyValueString);
        requiredDependencyValueString = requiredDependencyValueString.replace(requiredIdleToLiveParameterPair.getKey(),
                                                                              castToString(module.getParameters()
                                                                                                 .get(requiredIdleToLiveParameterPair.getValue())));
        requiredDependencyValueString = resourceParameterValue.replace(reference.getMatchedPattern(), requiredDependencyValueString);

        resourceParameter.setValue(requiredDependencyValueString);
    }

    private void resolveModuleProvidedDependency(Map<String, Object> requiredDependencyMap, Module module) {
        requiredDependencyMap.entrySet()
                             .forEach(entry -> updateModuleDependencyFromMap(module, entry));
    }

    private void updateModuleDependencyFromMap(Module module, Map.Entry<String, Object> entry) {
        if (isStringValueReplaceable(entry.getValue())) {
            var idleToLiveParameterPair = getMapEntryOnCondition(castToString(entry.getValue()));
            entry.setValue(castToString(entry.getValue()).replace(idleToLiveParameterPair.getKey(), castToString(module.getParameters()
                                                                                                                       .get(idleToLiveParameterPair.getValue()))));
        } else if (entry.getValue() instanceof Map) {
            resolveModuleProvidedDependency(castToMap(entry.getValue()), module);
        }
    }

    private Map.Entry<String, String> getMapEntryOnCondition(String replaceableString) {
        return idleToLiveParameterPairs.entrySet()
                                       .stream()
                                       .filter(idleToLiveParameterPair -> replaceableString.contains(idleToLiveParameterPair.getKey()))
                                       .findFirst()
                                       .get();
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
