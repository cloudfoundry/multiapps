package org.cloudfoundry.multiapps.mta.resolvers.v2;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.*;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.*;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Reference;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class DescriptorPlaceholderResolver extends PlaceholderResolver<DeploymentDescriptor> {

    private static final String CONFIG = "config";
    private static final String DEFAULT_URL = "${default-url}";
    private static final String DEFAULT_LIVE_URL = "default-live-url";
    protected final DeploymentDescriptor deploymentDescriptor;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;
    protected final ParametersChainBuilder parametersChainBuilder;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
                                         ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping,
                                         Set<String> dynamicResolvableParameters) {
        super("", "", singularToPluralMapping, dynamicResolvableParameters);
        this.deploymentDescriptor = descriptor;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
        this.parametersChainBuilder = new ParametersChainBuilder(descriptor, null);
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        deploymentDescriptor.setModules(getResolvedModules());
        deploymentDescriptor.setResources(getResolvedResources());
        deploymentDescriptor.setParameters(getResolvedProperties(deploymentDescriptor.getParameters()));
        return deploymentDescriptor;
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> propertiesToResolve) {
        List<Map<String, Object>> parametersList = Collections.singletonList(deploymentDescriptor.getParameters());
        addSingularParametersIfNecessary(parametersList);
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder,
                                                 dynamicResolvableParameters).resolve(propertiesToResolve,
                                                                                      PropertiesUtil.mergeProperties(parametersList),
                                                                                      prefix);
    }

    protected ResourcePlaceholderResolver getResourceResolver(Resource resource) {
        return new ResourcePlaceholderResolver(resource,
                                               prefix,
                                               parametersChainBuilder,
                                               propertiesResolverBuilder,
                                               parametersResolverBuilder,
                                               singularToPluralMapping,
                                               dynamicResolvableParameters);
    }

    protected List<Resource> getResolvedResources() {
        deploymentDescriptor.getResources()
                            .forEach(this::resolveResource);
        return deploymentDescriptor.getResources()
                                   .stream()
                                   .map(resource -> getResourceResolver(resource).resolve())
                                   .collect(Collectors.toList());
    }

    private void resolveResource(Resource resource) {
        if (resource.getParameters()
                    .containsKey(CONFIG)) {
            Map<String, Object> resourceParameters = castToMap(resource.getParameters()
                                                                       .get(CONFIG));
            resourceParameters.entrySet()
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
        List<Reference> references = FULLY_QUALIFIED.match(resourceParameterValue);

        references.forEach(ref -> updateParamsFromReference(resource, ref, resourceParameterValue, resourceParameter));
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
        Optional<ProvidedDependency> providedDependencyOpt = module.getProvidedDependencies()
                                                                   .stream()
                                                                   .filter(dp -> dp.getName()
                                                                                   .equals(ref.getDependencyName()))
                                                                   .findAny();
        Optional<RequiredDependency> requiredDependencyOpt = resource.getRequiredDependencies()
                                                                     .stream()
                                                                     .filter(reqDp -> reqDp.getName()
                                                                                           .equals(ref.getDependencyName()))
                                                                     .findAny();

        if (providedDependencyOpt.isPresent() && requiredDependencyOpt.isPresent()) {
            Object requiredDependencyValue = providedDependencyOpt.get()
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

    private Resource resolveResourceWithVersion(Resource resource) {
        return getResourceResolver(resource).resolve();
    }

    private String castToString(Object object) {
        return String.valueOf(object);
    }

    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module,
                                             prefix,
                                             parametersChainBuilder,
                                             propertiesResolverBuilder,
                                             parametersResolverBuilder,
                                             singularToPluralMapping,
                                             dynamicResolvableParameters);
    }

    protected List<Module> getResolvedModules() {
        return deploymentDescriptor.getModules()
                                   .stream()
                                   .map(module -> getModuleResolver(module).resolve())
                                   .collect(Collectors.toList());
    }

}
