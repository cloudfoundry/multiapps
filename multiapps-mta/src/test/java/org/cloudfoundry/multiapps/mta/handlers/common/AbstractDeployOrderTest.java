package org.cloudfoundry.multiapps.mta.handlers.common;

import java.util.Arrays;
import java.util.List;

import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;

public abstract class AbstractDeployOrderTest {

    public static final String PARALLEL_DEPLOYMENTS_PROP = "parallel-deployments";
    public static final String DEPENDENCY_TYPE_PROP = "dependency-type";
    public static final String DEPENDENCY_TYPE_HARD = "hard";

    protected final DescriptorHandler handler = getDescriptorHandler();

    protected abstract DescriptorHandler getDescriptorHandler();

    public void executeTestGetSortedModules(Tester tester, String deploymentDescriptorLocation, Expectation expectation) {
        String deploymentDescriptorYaml = TestUtil.getResourceAsString(deploymentDescriptorLocation, getClass());
        DeploymentDescriptor deploymentDescriptor = new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);

        tester.test(() -> {
            return Arrays.toString(getNames(handler.getModulesForDeployment(deploymentDescriptor, PARALLEL_DEPLOYMENTS_PROP,
                                                                            DEPENDENCY_TYPE_PROP, DEPENDENCY_TYPE_HARD)));
        }, expectation);
    }

    private String[] getNames(List<? extends Module> modules) {
        return modules.stream()
                      .map(Module::getName)
                      .toArray(String[]::new);
    }

}
