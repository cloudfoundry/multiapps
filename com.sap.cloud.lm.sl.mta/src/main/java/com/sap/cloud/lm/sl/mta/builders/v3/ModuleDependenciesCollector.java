package com.sap.cloud.lm.sl.mta.builders.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;

public class ModuleDependenciesCollector extends com.sap.cloud.lm.sl.mta.builders.v2.ModuleDependenciesCollector {

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor) {
        super(descriptor, null);
    }

    @Override
    public Set<String> collect(Module module) {
        Set<String> collectedDependencies = super.collect(module);
        com.sap.cloud.lm.sl.mta.model.v3.Module moduleV3 = cast(module);
        List<String> deployedAfter = collectedDependencies.stream()
            .collect(Collectors.toList());
        moduleV3.setDeployedAfter(deployedAfter);
        return collectedDependencies;
    }

    @Override
    protected List<String> getDependencies(Module module) {
        com.sap.cloud.lm.sl.mta.model.v3.Module moduleV3 = cast(module);
        return ListUtils.emptyIfNull(moduleV3.getDeployedAfter());
    }

    @Override
    protected Module findModuleSatisfyingDependency(String dependency) {
        return descriptor.getModules1()
            .stream()
            .filter(module -> module.getName()
                .equals(dependency))
            .findFirst()
            .orElse(null);
    }
}