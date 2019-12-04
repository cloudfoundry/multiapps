package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation.Type;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;

public class DescriptorHandlerTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandlerTest {

    public static class DeployOrderSequentialTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandlerTest.DeployOrderTest {

        public DeployOrderSequentialTest(String descriptorLocation, Expectation expectation) {
            super(descriptorLocation, expectation);
        }

        protected DescriptorHandler getDescriptorHandler() {
            return new DescriptorHandler();
        }

        @Parameters
        public static Iterable<Object[]> getParameters() {
            return Arrays.asList(new Object[][] {
// @formatter:off
                // (00)
                {
                    "mtad-05.yaml", new Expectation("[bar, foo, baz, qux]"),
                },
                // (01)
                {
                    "mtad-06.yaml", new Expectation("[bar, foo]"),
                },
                // (02)
                {
                    "mtad-07.yaml", new Expectation("[foo, bar]"),
                },
                // (03) The broker has a transitive dependency to the backend:
                {
                    "mtad-08.yaml", new Expectation("[db, service, backend, dashboard, broker]"),
                },
                // (04) There is a direct circular dependency between two modules, but  one of the modules  is soft:
                {
                    "mtad-09.yaml", new Expectation("[bar, baz, foo]"),
                },
                // (05) There is a direct circular dependency between two modules, and both of the modules are hard:
                {
                    "mtad-10.yaml", new Expectation(Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
                },
                // (06) There is a direct circular dependency between two modules, and both of the modules are soft:
                {
                    "mtad-13.yaml", new Expectation("[bar, foo, baz]"),
                },
                // (07) There is a circular transitive dependency between two modules, and both of the modules are hard:
                {
                    "mtad-11.yaml", new Expectation(Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
                },
                // (08) There is a circular transitive dependency between two modules, and both of the modules are hard:
                {
                    "mtad-14.yaml", new Expectation("[foo, bar, baz]"),
                },
                // (09) A module requires something from the same module:
                {
                    "mtad-12.yaml", new Expectation("[di-core-db, di-local-npm-registry, di-core, di-builder, di-runner]"),
                },
                // (10) There are two circular dependencies and all modules are soft:
                {
                    "mtad-15.yaml", new Expectation("[foo, bar, baz, qux, quux]"),
                },
                // (11) There are two circular dependencies and two modules are hard:
                {
                    "mtad-16.yaml", new Expectation(Type.EXCEPTION, "Modules, \"baz\" and \"bar\", have hard circular dependencies"),
                },
                // (12) There are two circular dependencies and one  module  is hard:
                {
                    "mtad-17.yaml", new Expectation("[bar, foo, baz, qux, quux]"),
                },
                // (13) There is a hard circular dependency between a module and itself:
                {
                    "mtad-18.yaml", new Expectation("[bar, foo]"),
                },
                // (14) The deployed-after attribute is used, therefore requires section is not used:
                {
                    "mtad-05-deployed-after.yaml", new Expectation("[baz, bar, foo, qux]"),
                },
// @formatter:on
            });
        }

        @Override
        protected DescriptorParser getDescriptorParser() {
            return new DescriptorParser();
        }

    }

    @RunWith(Parameterized.class)
    public static class DeployOrderParallelTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandlerTest.DeployOrderTest {

        public DeployOrderParallelTest(String descriptorLocation, Expectation expectation) {
            super(descriptorLocation, expectation);
        }

        protected DescriptorHandler getDescriptorHandler() {
            return new DescriptorHandler();
        }

        @Parameters
        public static Iterable<Object[]> getParameters() {
            return Arrays.asList(new Object[][] {
// @formatter:off
                // (00)
                {
                    "mtad-05-deployed-after-with-parallel-deployments.yaml", new Expectation("{bar=[], foo=[bar], baz=[foo, bar], qux=[]}"),
                },
                // (01)
                {
                    "mtad-06-deployed-after-with-parallel-deployments.yaml", new Expectation("{bar=[], foo=[bar]}"),
                },
                // (02)
                {
                    "mtad-07-deployed-after-with-parallel-deployments.yaml", new Expectation("{foo=[], bar=[foo]}"),
                },
                // (03) The broker has a transitive dependency to the backend:
                {
                    "mtad-08-deployed-after-with-parallel-deployments.yaml", new Expectation("{db=[], service=[db], backend=[], dashboard=[service, db, backend], broker=[dashboard, service, db, backend]}"),
                },
                // (05) Circular dependencies
                {
                    "mtad-10-deployed-after-with-parallel-deployments.yaml", new Expectation(Type.EXCEPTION, "Modules, \"baz\" and \"foo\", both depend on each others for deployment"),
                },
                // (07) Circular transitive dependencies
                {
                    "mtad-11-deployed-after-with-parallel-deployments.yaml", new Expectation(Type.EXCEPTION, "Modules, \"bar\" and \"foo\", both depend on each others for deployment"),
                },
                // (13) A module depends on itself
                {
                    "mtad-18-deployed-after-with-parallel-deployments.yaml", new Expectation("{bar=[], foo=[bar]}"),
                },
// @formatter:on
            });
        }

        @Override
        @Test
        public void testGetSortedModules() {
            final DeploymentDescriptor descriptor = getDescriptorParser().parseDeploymentDescriptorYaml(TestUtil.getResourceAsString(descriptorLocation,
                                                                                                                                     getClass()));

            tester.test(new Callable<String>() {

                @Override
                public String call() {
                    return getDeployedAfterMapString(handler.getModulesForDeployment(descriptor, PARALLEL_DEPLOYMENTS_PROP,
                                                                                     DEPENDENCY_TYPE_PROP, DEPENDENCY_TYPE_HARD));
                }

                private String getDeployedAfterMapString(List<? extends Module> modules) {
                    Map<String, List<String>> deployedAfterMap = new LinkedHashMap<>();
                    for (Module module : modules) {
                        deployedAfterMap.put(module.getName(), ListUtils.emptyIfNull(module.getDeployedAfter()));
                    }
                    return deployedAfterMap.toString();
                }

            }, expectation);
        }

        @Override
        protected DescriptorParser getDescriptorParser() {
            return new DescriptorParser();
        }

    }

}
