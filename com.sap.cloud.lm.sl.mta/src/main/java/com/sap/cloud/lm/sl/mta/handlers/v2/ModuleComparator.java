package com.sap.cloud.lm.sl.mta.handlers.v2;

import com.sap.cloud.lm.sl.mta.model.v1.Module;

public class ModuleComparator extends com.sap.cloud.lm.sl.mta.handlers.v1.ModuleComparator {

    public ModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        super(dependencyTypeProperty, hardDependencyType);
    }

    @Override
    protected Object getPropertyValue(Module module, String key) {
        return ((com.sap.cloud.lm.sl.mta.model.v2.Module) module).getParameters()
            .get(key);
    }

}
