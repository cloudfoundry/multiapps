package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.PropertiesPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class DescriptorPlaceholderResolver extends PlaceholderResolver<DeploymentDescriptor> {

    protected final DeploymentDescriptor deploymentDescriptor;

    protected final Platform platform;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    protected final ParametersChainBuilder parametersChainBuilder;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, Platform platform, SystemParameters systemParameters,
        ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        super("", "", systemParameters);
        this.deploymentDescriptor = descriptor;
        this.platform = platform;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
        this.parametersChainBuilder = new ParametersChainBuilder(descriptor, platform);
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        deploymentDescriptor.setModules2(ListUtil.upcastUnmodifiable(getResolvedModules()));
        deploymentDescriptor.setResources2(getResolvedResources());
        deploymentDescriptor.setParameters(getResolvedProperties(deploymentDescriptor.getParameters()));
        return deploymentDescriptor;
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> propertiesToResolve) {
        List<Map<String, Object>> parametersList = PropertiesUtil.getParametersList(platform,
            PropertiesUtil.asParametersProvider(systemParameters.getGeneralParameters()));
        addSingularParametersIfNecessary(parametersList);
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder) //
            .resolve(propertiesToResolve, PropertiesUtil.mergeProperties(parametersList), prefix);
    }

    protected ResourcePlaceholderResolver getResourceResolver(Resource resource) {
        return new ResourcePlaceholderResolver(resource, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder,
            parametersResolverBuilder);
    }

    protected List<Resource> getResolvedResources() {
        List<Resource> result = new ArrayList<>();
        for (Resource resource : deploymentDescriptor.getResources2()) {
            result.add(getResourceResolver(resource).resolve());
        }
        return result;
    }

    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder,
            parametersResolverBuilder);
    }

    protected List<? extends Module> getResolvedModules() {
        List<Module> result = new ArrayList<>();
        for (Module module : deploymentDescriptor.getModules2()) {
            result.add(getModuleResolver(module).resolve());
        }
        return result;
    }

}
