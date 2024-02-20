package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.Reference;
import org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;

public class ResourceLiveParameterResolver implements Resolver<Resource> {

    private static final String RESOURCE_PARAMETER_KEY_CONFIG = "config";
    private final DeploymentDescriptor deploymentDescriptor;
    private final Map<String, String> idleToLiveParameterPairs;
    private Resource resource;

    public ResourceLiveParameterResolver(DeploymentDescriptor deploymentDescriptor, Resource resource,
                                         Map<String, String> idleToLiveParameterPairs) {
        this.deploymentDescriptor = deploymentDescriptor;
        this.idleToLiveParameterPairs = idleToLiveParameterPairs;
        this.resource = resource;
    }

    @Override
    public Resource resolve() {
        if (resource.getParameters()
                    .containsKey(RESOURCE_PARAMETER_KEY_CONFIG)) {
            Map<String, Object> serviceCreationParameters = MiscUtil.<Map<String, Object>> cast(resource.getParameters()
                                                                                                        .get(RESOURCE_PARAMETER_KEY_CONFIG));
            serviceCreationParameters.entrySet()
                                     .forEach(this::resolveResourceParameter);
        }
        return resource;
    }

    private void resolveResourceParameter(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Map) {
            MiscUtil.<Map<String, Object>> cast(entry.getValue())
                    .entrySet()
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
        Optional<ProvidedDependency> moduleProvidedDependencyOpt = getModuleProvidedDependency(reference, module);

        if (moduleProvidedDependencyOpt.isPresent()) {
            Object requiredDependencyValue = moduleProvidedDependencyOpt.get()
                                                                        .getProperties()
                                                                        .get(reference.getKey());

            if (requiredDependencyValue instanceof Map) {
                resolveModuleProvidedDependency(MiscUtil.cast(requiredDependencyValue), module);
                resourceParameter.setValue(requiredDependencyValue);
            } else if (resourceParameterValue instanceof String
                && doesParameterContainDefaultPlaceholder(castToString(requiredDependencyValue))) {
                replaceValueInResourceParameter(module, resourceParameterValue, resourceParameter, reference,
                                                castToString(requiredDependencyValue));
            }
        }
    }

    private Optional<ProvidedDependency> getModuleProvidedDependency(Reference reference, Module module) {
        return module.getProvidedDependencies()
                     .stream()
                     .filter(providedDependency -> providedDependency.getName()
                                                                     .equals(reference.getDependencyName()))
                     .findAny();
    }

    private boolean doesParameterContainDefaultPlaceholder(String parameter) {
        return idleToLiveParameterPairs.keySet()
                                       .stream()
                                       .anyMatch(parameter::contains);
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
            resolveModuleProvidedDependency(MiscUtil.cast(entry.getValue()), module);
        } else if (entry.getValue() instanceof String && doesParameterContainDefaultPlaceholder(castToString(entry.getValue()))) {
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
