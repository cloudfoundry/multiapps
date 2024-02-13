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

    private static final String RESOURCE_PARAMETER_KEY_CONFIG = "config";
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

    public void resolve(Resource resource) {
        if (resource.getParameters()
                    .containsKey(RESOURCE_PARAMETER_KEY_CONFIG)) {
            Map<String, Object> serviceCreationParameters = castToMap(resource.getParameters()
                                                                              .get(RESOURCE_PARAMETER_KEY_CONFIG));
            serviceCreationParameters.entrySet()
                                     .forEach(this::resolveResourceParameter);
        }
    }

    private Map<String, Object> castToMap(Object entryValue) {
        return (Map<String, Object>) entryValue;
    }

    private void resolveResourceParameter(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Map) {
            castToMap(entry.getValue()).entrySet()
                                       .forEach(this::resolveResourceParameter);
        } else if (entry.getValue() instanceof String) {
            resolveConfigParameter(entry);
        }
    }

    private void resolveConfigParameter(Map.Entry<String, Object> resourceParameter) {
        String resourceParameterValue = (String) resourceParameter.getValue();
        List<Reference> matchedReferences = ReferencePattern.FULLY_QUALIFIED.match(resourceParameterValue);

        matchedReferences.forEach(reference -> updateParamsFromReference(reference, resourceParameterValue, resourceParameter));
    }

    private void updateParamsFromReference(Reference reference, String resourceParameterValue,
                                           Map.Entry<String, Object> resourceParameter) {
        deploymentDescriptor.getModules()
                            .stream()
                            .filter(module -> isModuleNeeded(module, reference.getDependencyName()))
                            .findFirst()
                            .ifPresent(module -> updateConfigParameterFromModule(reference, module, resourceParameterValue,
                                                                                 resourceParameter));
    }

    private void updateConfigParameterFromModule(Reference reference, Module module, String resourceParameterValue,
                                                 Map.Entry<String, Object> resourceParameter) {
        Optional<ProvidedDependency> moduleProvidedDependencyOpt = module.getProvidedDependencies()
                                                                         .stream()
                                                                         .filter(providedDependency -> providedDependency.getName()
                                                                                                                         .equals(reference.getDependencyName()))
                                                                         .findAny();

        if (moduleProvidedDependencyOpt.isPresent()) {
            Object requiredDependencyValue = moduleProvidedDependencyOpt.get()
                                                                        .getProperties()
                                                                        .get(reference.getKey());

            if (requiredDependencyValue instanceof Map) {
                resolveModuleProvidedDependency(castToMap(requiredDependencyValue), module);
                resourceParameter.setValue(requiredDependencyValue);
            } else if (resourceParameterValue instanceof String && doesParameterContainDefaultPlaceholder(requiredDependencyValue)) {
                replaceValueInResourceParameter(module, resourceParameterValue, resourceParameter, reference,
                                                castToString(requiredDependencyValue));
            }
        }
    }

    private boolean doesParameterContainDefaultPlaceholder(Object object) {
        return idleToLiveParameterPairs.keySet()
                                       .stream()
                                       .anyMatch(defaultParamPlaceholder -> castToString(object).contains(defaultParamPlaceholder));
    }

    private void replaceValueInResourceParameter(Module module, String resourceParameterValue, Map.Entry<String, Object> resourceParameter,
                                                 Reference reference, String requiredDependencyValueString) {
        Map.Entry<String, String> requiredIdleToLiveParameterPair = matchPlaceholderInParameter(requiredDependencyValueString);
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
        if (entry.getValue() instanceof Map) {
            resolveModuleProvidedDependency(castToMap(entry.getValue()), module);
        } else if (entry.getValue() instanceof String && doesParameterContainDefaultPlaceholder(entry.getValue())) {
            var matchedPlaceholderEntry = matchPlaceholderInParameter(castToString(entry.getValue()));
            String replacedParameter = castToString(entry.getValue()).replace(matchedPlaceholderEntry.getKey(),
                                                                              castToString(module.getParameters()
                                                                                                 .get(matchedPlaceholderEntry.getValue())));
            entry.setValue(replacedParameter);
        }
    }

    private Map.Entry<String, String> matchPlaceholderInParameter(String replaceableString) {
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
