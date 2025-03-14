package org.cloudfoundry.multiapps.mta.handlers.v3.handler;

import java.util.stream.Stream;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.handlers.v2.handler.DeployOrderTest;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.junit.jupiter.params.provider.Arguments;

public class DeployOrderSequentialTest extends DeployOrderTest {

    private static final String BASE_PATH = "/org/cloudfoundry/multiapps/mta/handlers/v3/";

    @Override
    protected DescriptorHandler getDescriptorHandler() {
        return new DescriptorHandler();
    }

    static Stream<Arguments> sortedModulesSource() {
        return Stream.of(Arguments.of(BASE_PATH + "mtad-05.yaml", new Tester.Expectation("[bar, foo, baz, qux]")),
                         Arguments.of(BASE_PATH + "mtad-06.yaml", new Tester.Expectation("[bar, foo]")),
                         Arguments.of(BASE_PATH + "mtad-07.yaml", new Tester.Expectation("[foo, bar]")),
                         // The broker has a transitive dependency to the backend:
                         Arguments.of(BASE_PATH + "mtad-08.yaml", new Tester.Expectation("[db, service, backend, dashboard, broker]")),
                         // There is a direct circular dependency between two modules, but one of the modules is soft:
                         Arguments.of(BASE_PATH + "mtad-09.yaml", new Tester.Expectation("[bar, baz, foo]")),
                         // There is a direct circular dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-10.yaml",
                                      new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                             "Modules, \"baz\" and \"foo\", have hard circular dependencies")),
                         // There is a direct circular dependency between two modules, and both of the modules are soft:
                         Arguments.of(BASE_PATH + "mtad-13.yaml", new Tester.Expectation("[bar, foo, baz]")),
                         // There is a circular transitive dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-11.yaml",
                                      new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                             "Modules, \"baz\" and \"foo\", have hard circular dependencies")),
                         // There is a circular transitive dependency between two modules, and both of the modules are hard:
                         Arguments.of(BASE_PATH + "mtad-14.yaml", new Tester.Expectation("[foo, bar, baz]")),
                         // A module requires something from the same module:
                         Arguments.of(BASE_PATH + "mtad-12.yaml",
                                      new Tester.Expectation("[di-core-db, di-local-npm-registry, di-core, di-builder, di-runner]")),
                         // There are two circular dependencies and all modules are soft:
                         Arguments.of(BASE_PATH + "mtad-15.yaml", new Tester.Expectation("[foo, bar, baz, qux, quux]")),
                         // There are two circular dependencies and two modules are hard:
                         Arguments.of(BASE_PATH + "mtad-16.yaml",
                                      new Tester.Expectation(Tester.Expectation.Type.EXCEPTION,
                                                             "Modules, \"baz\" and \"bar\", have hard circular dependencies")),
                         // There are two circular dependencies and one module is hard:
                         Arguments.of(BASE_PATH + "mtad-17.yaml", new Tester.Expectation("[bar, foo, baz, qux, quux]")),
                         // There is a hard circular dependency between a module and itself:
                         Arguments.of(BASE_PATH + "mtad-18.yaml", new Tester.Expectation("[bar, foo]")),
                         // TODO: Enable this test as part of LMCROSSITXSADEPLOY-1935.
                         // The deployed-after attribute is used, therefore requires section is not used. Test whether comparator
                         // handles transitive relations:
                         // Arguments.of("mtad-deployed-after-transitive-relations.yaml", new Expectation("[qux, foo, bar, baz]")),
                         // The deployed-after attribute is used, therefore requires section is not used:
                         Arguments.of(BASE_PATH + "mtad-deployed-after.yaml", new Tester.Expectation("[baz, bar, foo, qux]")));
    }

}
