package com.sap.cloud.lm.sl.mta.handlers.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.Target;

@RunWith(Enclosed.class)
public class DescriptorHandlerTest {

    public static class DescriptorHandlerTest1 {

        public static final String TARGET_1 = "XS-TEST";
        public static final String TARGET_2 = "CF-QUAL";
        public static final String TARGET_3 = "CF-PROD";
        public static final String TARGET_4 = "XS-QUAL";

        public static final String PLATFORM_1 = "CF";
        public static final String PLATFORM_2 = "XS";

        private final DescriptorHandler handler = new DescriptorHandler();

        @Test
        public void testFindTargetWhenItDoesNotExist() {
            assertNull(handler.findTarget(Collections.<Target> emptyList(), TARGET_1));
        }

        @Test
        public void testFindTargetWhenItExists() {
            List<Target> targets = new ArrayList<>();

            Target.Builder targetBuilder1 = new Target.Builder();
            Target.Builder targetBuilder2 = new Target.Builder();
            Target.Builder defaultTargetBuilder = new Target.Builder();

            targetBuilder1.setName(TARGET_1);
            targetBuilder2.setName(TARGET_2);
            defaultTargetBuilder.setName(TARGET_3);
            targetBuilder1.setType(PLATFORM_2);
            targetBuilder2.setType(PLATFORM_1);
            defaultTargetBuilder.setType(PLATFORM_1);

            Target defaultTarget = defaultTargetBuilder.build();

            targets.add(targetBuilder1.build());
            targets.add(targetBuilder2.build());
            targets.add(defaultTarget);

            assertEquals(handler.findTarget(targets, TARGET_1), targets.get(0));
            assertEquals(handler.findTarget(targets, TARGET_2), targets.get(1));
            assertEquals(handler.findTarget(targets, TARGET_3, defaultTarget), defaultTarget);
            assertEquals(handler.findTarget(targets, TARGET_4, defaultTarget), defaultTarget);
        }

        @Test
        public void testFindPlatformWhenItDoesNotExist() {
            assertNull(handler.findPlatform(Collections.<Platform> emptyList(), null));
            assertNull(handler.findPlatform(Collections.<Platform> emptyList(), PLATFORM_1));
        }

        @Test
        public void testFindPlatformWhenItExists() {
            List<Platform> platforms = new ArrayList<>();

            Platform.Builder platformBuilder1 = new Platform.Builder();
            Platform.Builder platformBuilder2 = new Platform.Builder();

            platformBuilder1.setName(PLATFORM_1);
            platformBuilder2.setName(PLATFORM_2);

            platforms.add(platformBuilder1.build());
            platforms.add(platformBuilder2.build());

            assertEquals(handler.findPlatform(platforms, PLATFORM_1), platforms.get(0));
            assertEquals(handler.findPlatform(platforms, null), platforms.get(0));
            assertEquals(handler.findPlatform(platforms, PLATFORM_2), platforms.get(1));
        }

    }

    @RunWith(Parameterized.class)
    public static class DescriptorHandlerTest2 {

        public static final String DEPENDENCY_TYPE_PROP = "dependency-type";
        public static final String DEPENDENCY_TYPE_HARD = "hard";

        private final DescriptorHandler handler = getDescriptorHandler();

        private String descriptorLocation;
        private Expectation expectation;

        public DescriptorHandlerTest2(String descriptorLocation, Expectation expectation) {
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
                    return Arrays.toString(getNames(handler.getSortedModules(descriptor, DEPENDENCY_TYPE_PROP, DEPENDENCY_TYPE_HARD)));
                }

                private String[] getNames(List<Module> modulles) {
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
