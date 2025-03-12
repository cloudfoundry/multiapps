package org.cloudfoundry.multiapps.mta.handlers.v3.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;
import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.handlers.v2.handler.DeployOrderTest;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DeployOrderParallelTest extends DeployOrderTest {

    private static final String BASE_PATH = "/org/cloudfoundry/multiapps/mta/handlers/v3/";
    private final Tester tester = Tester.forClass(getClass());

    @Override
    protected DescriptorHandler getDescriptorHandler() {
        return new DescriptorHandler();
    }

    @ParameterizedTest
    @MethodSource("sortedModulesSource")
    public void testGetSortedModules(String deploymentDescriptorLocation, Tester.Expectation expectation) {
        String deploymentDescriptorYaml = TestUtil.getResourceAsString(deploymentDescriptorLocation, getClass());
        DeploymentDescriptor deploymentDescriptor = new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);

        tester.test(() -> {
            return getDeployedAfterMapString(handler.getModulesForDeployment(deploymentDescriptor, PARALLEL_DEPLOYMENTS_PROP,
                                                                             DEPENDENCY_TYPE_PROP, DEPENDENCY_TYPE_HARD));
        }, expectation);
    }

    static Stream<Arguments> sortedModulesSource() {
        return Stream.of(Arguments.of(BASE_PATH + "mtad-05-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation("{bar=[], foo=[bar], baz=[foo, bar], qux=[]}")),
                         Arguments.of(BASE_PATH + "mtad-06-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation("{bar=[], foo=[bar]}")),
                         Arguments.of(BASE_PATH + "mtad-07-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation("{foo=[], bar=[foo]}")),
                         // The broker has a transitive dependency to the backend:
                         Arguments.of(BASE_PATH + "mtad-08-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation("{db=[], service=[db], backend=[], dashboard=[service, db, backend], broker=[dashboard, service, db, backend]}")),
                         // Circular dependencies:
                         Arguments.of(BASE_PATH + "mtad-10-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                             "Modules, \"baz\" and \"foo\", both depend on each others for deployment")),
                         // Circular transitive dependencies:
                         Arguments.of(BASE_PATH + "mtad-11-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                             "Modules, \"bar\" and \"foo\", both depend on each others for deployment")),
                         // A module depends on itself:
                         Arguments.of(BASE_PATH + "mtad-18-deployed-after-with-parallel-deployments.yaml",
                                      new Tester.Expectation("{bar=[], foo=[bar]}")));
    }

    private String getDeployedAfterMapString(List<? extends Module> modules) {
        Map<String, List<String>> deployedAfterMap = new LinkedHashMap<>();
        for (Module module : modules) {
            deployedAfterMap.put(module.getName(), ListUtils.emptyIfNull(module.getDeployedAfter()));
        }
        return deployedAfterMap.toString();
    }
}
