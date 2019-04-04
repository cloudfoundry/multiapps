package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;

import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.Module;

public class ModuleComparator implements Comparator<Entry<Module, Set<String>>> {

    private final String dependencyTypeProperty;
    private final String hardDependencyType;

    public ModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        this.dependencyTypeProperty = dependencyTypeProperty;
        this.hardDependencyType = hardDependencyType;
    }

    @Override
    public int compare(Entry<Module, Set<String>> pair1, Entry<Module, Set<String>> pair2) {
        Module module1 = pair1.getKey();
        Set<String> module1Dependencies = pair1.getValue();
        String dependencyTypeModule1 = (String) getPropertyValue(module1, dependencyTypeProperty);

        Module module2 = pair2.getKey();
        Set<String> module2Dependencies = pair2.getValue();
        String dependencyTypeModule2 = (String) getPropertyValue(module2, dependencyTypeProperty);

        if (circularDependencyExists(module1.getName(), module1Dependencies, module2.getName(), module2Dependencies)) {
            if (hardDependencyType.equals(dependencyTypeModule1) && hardDependencyType.equals(dependencyTypeModule2)) {
                throw new IllegalStateException(
                    MessageFormat.format(Messages.MULTIPLE_HARD_MODULES_DETECTED, module1.getName(), module2.getName()));
            }
            if (hardDependencyType.equals(dependencyTypeModule1)) {
                return -1;
            }
            if (hardDependencyType.equals(dependencyTypeModule2)) {
                return +1;
            }
            return +0;
        }
        if (module1Dependencies.contains(module2.getName())) {
            return +1;
        }
        if (module2Dependencies.contains(module1.getName())) {
            return -1;
        }
        return +0;
    }

    private boolean circularDependencyExists(String module1Name, Set<String> module1Dependencies, String module2Name,
        Set<String> module2Dependencies) {
        return module1Dependencies.contains(module2Name) && module2Dependencies.contains(module1Name);
    }

    protected Object getPropertyValue(Module module, String key) {
        return module.getParameters()
            .get(key);
    }

}
