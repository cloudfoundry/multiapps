package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;

@RunWith(Enclosed.class)
public class DescriptorHandlerTest {

    @RunWith(Parameterized.class)
    public static class DeployOrderTest {

        public static final String PARALLEL_DEPLOYMENTS_PROP = "parallel-deployments";
        public static final String DEPENDENCY_TYPE_PROP = "dependency-type";
        public static final String DEPENDENCY_TYPE_HARD = "hard";

        protected final DescriptorHandler handler = getDescriptorHandler();

        protected String descriptorLocation;
        protected Expectation expectation;

        public DeployOrderTest(String descriptorLocation, Expectation expectation) {
            this.descriptorLocation = descriptorLocation;
            this.expectation = expectation;
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
                    "mtad-01.yaml", new Expectation("[bar, foo, baz, qux]"),
                },
                // (01)
                {
                    "mtad-02.yaml", new Expectation("[bar, foo]"),
                },
                // (02)
                {
                    "mtad-14.yaml", new Expectation("[foo, bar]"),
                },
                // (03) The broker has a transitive dependency to the backend:
                {
                    "mtad-12.yaml", new Expectation("[db, service, backend, dashboard, broker]"),
                },
                // (04) There is a direct circular dependency between two modules, but  one of the modules  is soft:
                {
                    "mtad-10.yaml", new Expectation("[bar, baz, foo]"),
                },
                // (05) There is a direct circular dependency between two modules, and both of the modules are hard:
                {
                    "mtad-11.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
                },
                // (06) There is a direct circular dependency between two modules, and both of the modules are soft:
                {
                    "mtad-16.yaml", new Expectation("[bar, foo, baz]"),
                },
                // (07) There is a circular transitive dependency between two modules, and both of the modules are hard:
                {
                    "mtad-13.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
                },
                // (08) There is a circular transitive dependency between two modules, and both of the modules are soft:
                {
                    "mtad-15.yaml", new Expectation("[foo, bar, baz]"),
                },
                // (09) There are two circular dependencies and all modules are soft:
                {
                    "mtad-17.yaml", new Expectation("[foo, bar, baz, qux, quux]"),
                },
                // (10) There are two circular dependencies and two modules are hard:
                {
                    "mtad-19.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"bar\", have hard circular dependencies"),
                },
                // (11) There are two circular dependencies and one  module  is hard:
                {
                    "mtad-18.yaml", new Expectation("[bar, foo, baz, qux, quux]"),
                },
                // (12) There is a hard circular dependency between a module and itself:
                {
                    "mtad-20.yaml", new Expectation("[bar, foo]"),
                },
// @formatter:on
            });
        }

        @Test
        public void testGetSortedModules() throws Exception {
            final DeploymentDescriptor descriptor = getDescriptorParser()
                .parseDeploymentDescriptorYaml(TestUtil.getResourceAsString(descriptorLocation, getClass()));

            TestUtil.test(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return Arrays.toString(getNames(handler.getModulesForDeployment(descriptor, PARALLEL_DEPLOYMENTS_PROP, DEPENDENCY_TYPE_PROP, DEPENDENCY_TYPE_HARD)));
                }

                private String[] getNames(List<? extends Module> modulles) {
                    List<String> names = new LinkedList<>();
                    for (Module module : modulles) {
                        names.add(module.getName());
                    }
                    return names.toArray(new String[0]);
                }

            }, expectation, getClass());
        }

        protected DescriptorParser getDescriptorParser() {
            return new DescriptorParser();
        }

    }

}
