package com.sap.cloud.lm.sl.mta.handlers.v1_0;

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
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform.PlatformBuilder;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target.TargetBuilder;

@RunWith(Enclosed.class)
public class DescriptorHandlerTest {

    public static class DescriptorHandlerTest1 {

        public static final String PLATFORM_1 = "XS-TEST";
        public static final String PLATFORM_2 = "CF-QUAL";
        public static final String PLATFORM_3 = "CF-PROD";
        public static final String PLATFORM_4 = "XS-QUAL";

        public static final String PLATFORM_TYPE_1 = "CF";
        public static final String PLATFORM_TYPE_2 = "XS";

        private final DescriptorHandler handler = new DescriptorHandler();

        @Test
        public void testFindPlatformWhenPlatformDoesNotExist() throws Exception {
            assertNull(handler.findTarget(Collections.<Target> emptyList(), PLATFORM_1));
        }

        @Test
        public void testFindPlatformWhenPlatformExists() throws Exception {
            List<Target> platforms = new ArrayList<Target>();

            TargetBuilder platformBuilder1 = new TargetBuilder();
            TargetBuilder platformBuilder2 = new TargetBuilder();
            TargetBuilder defaultPlatformBuilder = new TargetBuilder();

            platformBuilder1.setName(PLATFORM_1);
            platformBuilder2.setName(PLATFORM_2);
            defaultPlatformBuilder.setName(PLATFORM_3);
            platformBuilder1.setType(PLATFORM_TYPE_2);
            platformBuilder2.setType(PLATFORM_TYPE_1);
            defaultPlatformBuilder.setType(PLATFORM_TYPE_1);

            Target defaultPlatform = defaultPlatformBuilder.build();

            platforms.add(platformBuilder1.build());
            platforms.add(platformBuilder2.build());
            platforms.add(defaultPlatform);

            assertEquals(handler.findTarget(platforms, PLATFORM_1), platforms.get(0));
            assertEquals(handler.findTarget(platforms, PLATFORM_2), platforms.get(1));
            assertEquals(handler.findTarget(platforms, PLATFORM_3, defaultPlatform), defaultPlatform);
            assertEquals(handler.findTarget(platforms, PLATFORM_4, defaultPlatform), defaultPlatform);
        }

        @Test
        public void testFindPlatformTypeWhenPlatformTypeDoesNotExist() throws Exception {
            assertNull(handler.findPlatform(Collections.<Platform> emptyList(), null));
            assertNull(handler.findPlatform(Collections.<Platform> emptyList(), PLATFORM_TYPE_1));
        }

        @Test
        public void testFindPlatformTypeWhenPlatformTypeExists() throws Exception {
            List<Platform> platformTypes = new ArrayList<Platform>();

            PlatformBuilder platformTypeBuilder1 = new PlatformBuilder();
            PlatformBuilder platformTypeBuilder2 = new PlatformBuilder();

            platformTypeBuilder1.setName(PLATFORM_TYPE_1);
            platformTypeBuilder2.setName(PLATFORM_TYPE_2);

            platformTypes.add(platformTypeBuilder1.build());
            platformTypes.add(platformTypeBuilder2.build());

            assertEquals(handler.findPlatform(platformTypes, PLATFORM_TYPE_1), platformTypes.get(0));
            assertEquals(handler.findPlatform(platformTypes, null), platformTypes.get(0));
            assertEquals(handler.findPlatform(platformTypes, PLATFORM_TYPE_2), platformTypes.get(1));
        }

    }

    @RunWith(Parameterized.class)
    public static class DescriptorHandlerTest2 {

        public static final String DEPENDENCY_TYPE_PROP = "dependency-type";
        public static final String DEPENDENCY_TYPE_HARD = "hard";

        private final DescriptorHandler handler = getDescriptorHandler();

        private String descriptorLocation;
        private String expected;

        public DescriptorHandlerTest2(String descriptorLocation, String expected) {
            this.descriptorLocation = descriptorLocation;
            this.expected = expected;
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
                    "mtad-01.yaml", "[bar, foo, baz, qux]",
                },
                // (01)
                {
                    "mtad-02.yaml", "[bar, foo]",
                },
                // (02)
                {
                    "mtad-14.yaml", "[foo, bar]",
                },
                // (03) The broker has a transitive dependency to the backend:
                {
                    "mtad-12.yaml", "[db, service, backend, dashboard, broker]",
                },
                // (04) There is a direct circular dependency between two modules, but  one of the modules  is soft:
                {
                    "mtad-10.yaml", "[bar, baz, foo]",
                },
                // (05) There is a direct circular dependency between two modules, and both of the modules are hard:
                {
                    "mtad-11.yaml", "E:Modules, \"baz\" and \"foo\", have hard circular dependencies",
                },
                // (06) There is a direct circular dependency between two modules, and both of the modules are soft:
                {
                    "mtad-16.yaml", "[bar, foo, baz]",
                },
                // (07) There is a circular transitive dependency between two modules, and both of the modules are hard:
                {
                    "mtad-13.yaml", "E:Modules, \"baz\" and \"foo\", have hard circular dependencies",
                },
                // (08) There is a circular transitive dependency between two modules, and both of the modules are soft:
                {
                    "mtad-15.yaml", "[foo, bar, baz]",
                },
                // (09) There are two circular dependencies and all modules are soft:
                {
                    "mtad-17.yaml", "[foo, bar, baz, qux, quux]",
                },
                // (10) There are two circular dependencies and two modules are hard:
                {
                    "mtad-19.yaml", "E:Modules, \"baz\" and \"bar\", have hard circular dependencies",
                },
                // (11) There are two circular dependencies and one  module  is hard:
                {
                    "mtad-18.yaml", "[bar, foo, baz, qux, quux]",
                },
                // (12) There is a hard circular dependency between a module and itself:
                {
                    "mtad-20.yaml", "[bar, foo]",
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

            }, expected, getClass(), false);
        }

        protected DescriptorParser getDescriptorParser() {
            return new DescriptorParser();
        }

    }

}
