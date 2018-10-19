package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.v3.Module;

public class ModuleComparator implements Comparator<Module> {

    @Override
    public int compare(Module module1, Module module2) {
        List<String> module1DeployedAfter = ListUtils.emptyIfNull(module1.getDeployedAfter());
        List<String> module2DeployedAfter = ListUtils.emptyIfNull(module2.getDeployedAfter());
        if (circularDependencyExists(module1.getName(), module2.getName(), module1DeployedAfter, module2DeployedAfter)) {
            throw new IllegalStateException(
                MessageFormat.format(Messages.CIRCULAR_DEPLOYMENT_DEPENDENCIES_DETECTED, module1.getName(), module2.getName()));
        }
        if (module1DeployedAfter.contains(module2.getName())) {
            return +1;
        }
        if (module2DeployedAfter.contains(module1.getName())) {
            return -1;
        }
        return +0;
    }

    private boolean circularDependencyExists(String module1Name, String module2Name, List<String> module1DeployedAfter,
        List<String> module2DeployedAfter) {
        return module1DeployedAfter.contains(module2Name) && module2DeployedAfter.contains(module1Name);
    }
}
