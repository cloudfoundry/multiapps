package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2_0.DescriptorPlaceholderResolver {

    protected final DeploymentDescriptor deploymentDescriptor;

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, Platform platform, Target target,
        SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        super(descriptor, platform, target, systemParameters, propertiesResolverBuilder, parametersResolverBuilder);
        this.deploymentDescriptor = descriptor;
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        deploymentDescriptor.setModules3_1(ListUtil.upcastUnmodifiable(getResolvedModules()));
        deploymentDescriptor.setResources2_0(getResolvedResources());
        deploymentDescriptor.setParameters(getResolvedProperties(deploymentDescriptor.getParameters()));
        return deploymentDescriptor;
    }

    @Override
    protected List<? extends Module> getResolvedModules() throws ContentException {
        List<Module> result = new ArrayList<Module>();
        for (Module module : deploymentDescriptor.getModules3_1()) {
            result.add(getModuleResolver(module).resolve());
        }
        return result;
    }

    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder,
            parametersResolverBuilder);
    }
}
