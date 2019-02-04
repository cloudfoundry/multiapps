package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorPlaceholderResolver {

    protected final DeploymentDescriptor deploymentDescriptor;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super(descriptor, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping);
        this.deploymentDescriptor = descriptor;
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        deploymentDescriptor.setModules3(ListUtil.upcastUnmodifiable(getResolvedModules()));
        deploymentDescriptor.setResources2(getResolvedResources());
        deploymentDescriptor.setParameters(getResolvedProperties(deploymentDescriptor.getParameters()));
        return deploymentDescriptor;
    }

    @Override
    protected List<? extends Module> getResolvedModules() {
        List<Module> result = new ArrayList<>();
        for (Module module : deploymentDescriptor.getModules3()) {
            result.add(getModuleResolver(module).resolve());
        }
        return result;
    }

    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module, prefix, parametersChainBuilder, propertiesResolverBuilder, parametersResolverBuilder,
            singularToPluralMapping);
    }
}
