package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.ResourceLiveParameterResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class DescriptorPlaceholderResolver extends PlaceholderResolver<DeploymentDescriptor> {

    protected final DeploymentDescriptor deploymentDescriptor;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;
    protected final ParametersChainBuilder parametersChainBuilder;
    private final ResourceLiveParameterResolverBuilder resourceLiveParameterResolverBuilder;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
                                         ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping,
                                         Set<String> dynamicResolvableParameters, Map<String, String> idleToLiveParameterPairs) {
        super("", "", singularToPluralMapping, dynamicResolvableParameters);
        this.deploymentDescriptor = descriptor;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
        this.parametersChainBuilder = new ParametersChainBuilder(descriptor, null);
        this.resourceLiveParameterResolverBuilder = new ResourceLiveParameterResolverBuilder(deploymentDescriptor, idleToLiveParameterPairs);
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
                                               dynamicResolvableParameters,
                                               resourceLiveParameterResolverBuilder);
    }

    protected List<Resource> getResolvedResources() {
        return deploymentDescriptor.getResources()
                                   .stream()
                                   .map(resource -> getResourceResolver(resource).resolve())
                                   .collect(Collectors.toList());
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
