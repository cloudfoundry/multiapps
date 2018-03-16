package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import com.sap.cloud.lm.sl.mta.model.v1_0.Module;

public class ModuleComparator extends com.sap.cloud.lm.sl.mta.handlers.v1_0.ModuleComparator {

    public ModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        super(dependencyTypeProperty, hardDependencyType);
    }

    @Override
    protected Object getPropertyValue(Module module, String key) {
        return ((com.sap.cloud.lm.sl.mta.model.v2_0.Module) module).getParameters()
            .get(key);
    }

}
