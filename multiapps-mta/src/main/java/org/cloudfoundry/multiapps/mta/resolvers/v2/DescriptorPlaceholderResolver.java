package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.*;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.*;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.*;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;
import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.PLACEHOLDER;

public class DescriptorPlaceholderResolver extends PlaceholderResolver<DeploymentDescriptor> {

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
                                                                                   PropertiesUtil.mergeProperties(parametersList), prefix);
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
        List<Resource> resources = deploymentDescriptor.getResources();

        for (Resource resource: resources) {
            if (resource.getParameters().containsKey("config")) {
                Map<String, Object> resourceParameters = (Map<String, Object>) resource.getParameters().get("config");

                for (Map.Entry<String, Object> resourceParameter: resourceParameters.entrySet()) {
                    if(resourceParameter.getValue() instanceof Map) {
                        resolveMapParameter((Map<String, Object>) resourceParameter.getValue(), resource);
                    } else if (resourceParameter.getValue() instanceof String) {
                        resolveConfigParameter(resourceParameter, resource);
                        //resourceParameters.put(resourceParameter.getKey(), resolvedParameter);
                    }
                }
            }
        }
        deploymentDescriptor.setResources(resources);
        return deploymentDescriptor.getResources()
                                   .stream()
                                   .map(resource -> getResourceResolver(resource).resolve())
                                   .collect(Collectors.toList());
    }

    private void resolveMapParameter(Map<String, Object> resourceParameter, Resource resource) {
        for (Map.Entry<String, Object> resourceParameterEntry: resourceParameter.entrySet()) {
            if (resourceParameterEntry.getValue() instanceof Map) {
                resolveMapParameter((Map<String, Object>) resourceParameterEntry.getValue(), resource);
            } else if (resourceParameterEntry.getValue() instanceof String) {
                resolveConfigParameter(resourceParameterEntry, resource);
            }
        }
    }

    private void resolveConfigParameter(Map.Entry<String, Object> resourceParameter, Resource resource) {
        String resourceParameterValue = (String) resourceParameter.getValue();
        List<Reference> references = FULLY_QUALIFIED.match(resourceParameterValue);

        if (!references.isEmpty()) {
            for (Reference reference : references) {

                Optional<Module> requiredModule = deploymentDescriptor.getModules().stream().filter(module -> getModule(module, reference.getDependencyName())).findFirst();

                if (requiredModule.isPresent()) {
                    Optional<ProvidedDependency> providedDependency = requiredModule.get().getProvidedDependencies().stream().filter(providedDependencyy -> providedDependencyy.getName().equals(reference.getDependencyName())).findAny();
                    Optional<RequiredDependency> requiredDependency = resource.getRequiredDependencies().stream().filter(requiredDependency1 -> requiredDependency1.getName().equals(reference.getDependencyName())).findAny();

                    if (providedDependency.isPresent() && requiredDependency.isPresent()) {
                        Object requiredDependencyValue = providedDependency.get().getProperties().get(reference.getKey());

                        if (requiredDependencyValue instanceof Map) {
                            resolveModuleProvidedDependency((Map<String, Object>) requiredDependencyValue, requiredModule.get());
                            resourceParameter.setValue(requiredDependencyValue);
                        } else if (requiredDependencyValue instanceof String) {
                            String requiredDependencyValueString = String.valueOf(requiredDependencyValue);
                            if (requiredDependencyValueString.contains("${default-url}")) {
                                requiredDependencyValueString = requiredDependencyValueString.replace("${default-url}", (String) requiredModule.get().getParameters().get("default-live-url"));

                                String b = resourceParameterValue.replace(reference.getMatchedPattern(), requiredDependencyValueString);
                                resourceParameter.setValue(b);
                            }
                        }
                    }
                }
            }
        }
    }

    private void resolveModuleProvidedDependency(Map<String, Object> requiredDependencyMap, Module module) {
        for (Map.Entry<String, Object> requiredDependencyEntry: requiredDependencyMap.entrySet()) {
            if (requiredDependencyEntry.getValue() instanceof Map) {
                resolveModuleProvidedDependency((Map<String, Object>) requiredDependencyEntry.getValue(), module);
            } else if (requiredDependencyEntry.getValue() instanceof String) {
                String requiredDependencyString = (String) requiredDependencyEntry.getValue();
                if (requiredDependencyString.contains("${default-url}")) {
                    requiredDependencyString = requiredDependencyString.replace("${default-url}", (String) module.getParameters().get("default-live-url"));
                    requiredDependencyEntry.setValue(requiredDependencyString);
                }
            }
        }
    }

    private boolean getModule(Module module, String name) {
        for (ProvidedDependency providedDependency: module.getProvidedDependencies()) {
            if (providedDependency.getName().equals(name)) {
                return true;
            }
        }

        return false;
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
