package org.cloudfoundry.multiapps.mta.handlers.v2.handler;

import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.common.test.Tester.Expectation;
import org.cloudfoundry.multiapps.mta.handlers.common.AbstractDeployOrderTest;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DeployOrderTest extends AbstractDeployOrderTest {

    private static final String BASE_PATH = "/org/cloudfoundry/multiapps/mta/handlers/v2/";
    private final Tester tester = Tester.forClass(getClass());

    @Override
    protected DescriptorHandler getDescriptorHandler() {
        return new DescriptorHandler();
    }

    @ParameterizedTest
    @MethodSource("sortedModulesSource")
    protected void testGetSortedModules(String deploymentDescriptorLocation, Expectation expectation) {
        executeTestGetSortedModules(tester, deploymentDescriptorLocation, expectation);
    }

    static Stream<Arguments> sortedModulesSource() {
        return Stream.of(Arguments.of(BASE_PATH + "mtad-05.yaml", new Expectation("[bar, foo, baz, qux]")),
                         Arguments.of(BASE_PATH + "mtad-06.yaml", new Expectation("[bar, foo]")),
                         Arguments.of(BASE_PATH + "mtad-07.yaml", new Expectation("[foo, bar]")),
                         Arguments.of(BASE_PATH + "mtad-08.yaml", new Expectation("[db, service, backend, dashboard, broker]")),
                         // There is a direct circular dependency between two modules, but one of the modules is soft:
                         Arguments.of(BASE_PATH + "mtad-09.yaml", new Expectation("[bar, baz, foo]")),
                         // There is a direct circular dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-10.yaml",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Modules, \"baz\" and \"foo\", have hard circular dependencies")),
                         // There is a direct circular dependency between two modules, and both of the modules are soft:
                         Arguments.of(BASE_PATH + "mtad-13.yaml", new Expectation("[bar, foo, baz]")),
                         // There is a circular transitive dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-11.yaml",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Modules, \"baz\" and \"foo\", have hard circular dependencies")),
                         // There is a circular transitive dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-14.yaml", new Expectation("[foo, bar, baz]")),
                         // A module requires something from the same module:
                         Arguments.of(BASE_PATH + "mtad-12.yaml",
                                      new Expectation("[di-core-db, di-local-npm-registry, di-core, di-builder, di-runner]")),
                         // There are two circular dependencies and all modules are soft:
                         Arguments.of(BASE_PATH + "mtad-15.yaml", new Expectation("[foo, bar, baz, qux, quux]")),
                         // There are two circular dependencies and two modules are hard:
                         Arguments.of(BASE_PATH + "mtad-16.yaml",
                                      new Expectation(Expectation.Type.EXCEPTION,
                                                      "Modules, \"baz\" and \"bar\", have hard circular dependencies")),
                         // There are two circular dependencies and one module is hard:
                         Arguments.of(BASE_PATH + "mtad-17.yaml", new Expectation("[bar, foo, baz, qux, quux]")),
                         // There is a hard circular dependency between a module and itself:
                         Arguments.of(BASE_PATH + "mtad-18.yaml", new Expectation("[bar, foo]")));
    }

}
