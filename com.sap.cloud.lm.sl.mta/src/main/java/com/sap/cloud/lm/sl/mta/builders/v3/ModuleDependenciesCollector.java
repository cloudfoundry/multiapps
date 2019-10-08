package com.sap.cloud.lm.sl.mta.builders.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import org.apache.commons.collections4.ListUtils;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;

public class ModuleDependenciesCollector extends com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector {

    private final com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler v3Handler;

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor, DescriptorHandler handler) {
        super(descriptor, null);
        v3Handler = (com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorHandler) handler;
    }

    @Override
    public Set<String> collect(Module module) {
        Set<String> collectedDependencies = super.collect(module);
        List<String> deployedAfter = new ArrayList<>(collectedDependencies);
        module.setDeployedAfter(deployedAfter);
        return collectedDependencies;
    }

    @Override
    protected List<String> getDependencies(Module module) {
        return ListUtils.emptyIfNull(module.getDeployedAfter());
    }

    @Override
    protected Module findModuleSatisfyingDependency(String dependency) {
        return v3Handler.findModule(descriptor, dependency);
    }
}