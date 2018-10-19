package com.sap.cloud.lm.sl.mta.builders.v2;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;

public class ModuleDependenciesCollector extends com.sap.cloud.lm.sl.mta.builders.v1.ModuleDependenciesCollector {

    public ModuleDependenciesCollector(DeploymentDescriptor descriptor, DescriptorHandler handler) {
        super(descriptor, handler);
    }

    @Override
    protected List<String> getDependencies(Module module) {
        com.sap.cloud.lm.sl.mta.model.v2.Module moduleV2 = cast(module);
        return moduleV2.getRequiredDependencies2()
            .stream()
            .map(RequiredDependency::getName)
            .collect(Collectors.toList());
    }
}
