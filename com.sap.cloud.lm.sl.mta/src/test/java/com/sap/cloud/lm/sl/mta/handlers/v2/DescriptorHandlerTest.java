package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

public class DescriptorHandlerTest extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandlerTest {

    @RunWith(Parameterized.class)
    public static class DeployOrderTest extends com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandlerTest.DeployOrderTest {

        public DeployOrderTest(String descriptorLocation, Expectation expectation) {
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
                    "mtad-10.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
                },
                // (06) There is a direct circular dependency between two modules, and both of the modules are soft:
                {
                    "mtad-13.yaml", new Expectation("[bar, foo, baz]"),
                },
                // (07) There is a circular transitive dependency between two modules, and both of the modules are hard:
                {
                    "mtad-11.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"foo\", have hard circular dependencies"),
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
                    "mtad-16.yaml", new Expectation(Expectation.Type.EXCEPTION, "Modules, \"baz\" and \"bar\", have hard circular dependencies"),
                },
                // (12) There are two circular dependencies and one  module  is hard:
                {
                    "mtad-17.yaml", new Expectation("[bar, foo, baz, qux, quux]"),
                },
                // (13) There is a hard circular dependency between a module and itself:
                {
                    "mtad-18.yaml", new Expectation("[bar, foo]"),
                },
// @formatter:on
            });
        }

        @Override
        protected DescriptorParser getDescriptorParser() {
            return new DescriptorParser();
        }

    }

}
