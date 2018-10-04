package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.v1.Module;

public class ModuleComparator implements Comparator<Pair<Module, Set<String>>> {

    private final String dependencyTypeProperty;
    private final String hardDependencyType;

    public ModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        this.dependencyTypeProperty = dependencyTypeProperty;
        this.hardDependencyType = hardDependencyType;
    }

    @Override
    public int compare(Pair<Module, Set<String>> modulePair1, Pair<Module, Set<String>> modulePair2) {
        Module module1 = modulePair1._1;
        Module module2 = modulePair2._1;
        String dependencyTypeModule1 = (String) getPropertyValue(module1, dependencyTypeProperty);
        String dependencyTypeModule2 = (String) getPropertyValue(module2, dependencyTypeProperty);
        Set<String> module1Dependencies = modulePair1._2;
        Set<String> module2Dependencies = modulePair2._2;

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
        Map<String, Object> properties = module.getProperties();
        return properties.get(key);
    }

}
