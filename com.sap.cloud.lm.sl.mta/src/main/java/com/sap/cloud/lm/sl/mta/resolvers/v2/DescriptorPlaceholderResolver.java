package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class DescriptorPlaceholderResolver extends PlaceholderResolver<DeploymentDescriptor> {

    protected final DeploymentDescriptor deploymentDescriptor;

    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    protected final ParametersChainBuilder parametersChainBuilder;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super("", "", singularToPluralMapping);
        this.deploymentDescriptor = descriptor;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
        this.parametersChainBuilder = new ParametersChainBuilder(descriptor, null);
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        deploymentDescriptor.setModules(ListUtil.upcastUnmodifiable(getResolvedModules()));
        deploymentDescriptor.setResources(getResolvedResources());
        deploymentDescriptor.setParameters(getResolvedProperties(deploymentDescriptor.getParameters()));
        return deploymentDescriptor;
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> propertiesToResolve) {
        List<Map<String, Object>> parametersList = Arrays.asList(deploymentDescriptor.getParameters());
        addSingularParametersIfNecessary(parametersList);
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder).resolve(propertiesToResolve,
            PropertiesUtil.mergeProperties(parametersList), prefix);
    }

    protected ResourcePlaceholderResolver getResourceResolver(Resource resource) {
        return new ResourcePlaceholderResolver(resource, prefix, parametersChainBuilder, propertiesResolverBuilder,
            parametersResolverBuilder, singularToPluralMapping);
    }

    protected List<Resource> getResolvedResources() {
        return deploymentDescriptor.getResources()
            .stream()
            .map(resource -> getResourceResolver(resource).resolve())
            .collect(Collectors.toList());
    }

    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module, prefix, parametersChainBuilder, propertiesResolverBuilder, parametersResolverBuilder,
            singularToPluralMapping);
    }

    protected List<Module> getResolvedModules() {
        return deploymentDescriptor.getModules()
            .stream()
            .map(module -> getModuleResolver(module).resolve())
            .collect(Collectors.toList());
    }

}
