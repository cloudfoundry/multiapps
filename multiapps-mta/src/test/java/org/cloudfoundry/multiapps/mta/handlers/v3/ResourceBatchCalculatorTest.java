package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.apache.commons.collections4.ListUtils;
import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResourceBatchCalculatorTest {

    protected final Tester tester = Tester.forClass(getClass());

    protected org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler getDescriptorHandler() {
        return new org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler();
    }

    public Stream<Arguments> groupResourcesByWeightTest() {
        return Stream.of(Arguments.of("mtad-00-processed-after.yaml",
                                      new Tester.Expectation("{serviceD=[], serviceB=[serviceD], serviceC=[serviceD], serviceA=[serviceB, serviceC]}")),
                         Arguments.of("mtad-01-processed-after.yaml",
                                      new Tester.Expectation("{serviceC=[], serviceD=[serviceC], serviceB=[serviceD], serviceA=[serviceB, serviceC, serviceD]}")),
                         Arguments.of("mtad-03-processed-after.yaml",
                                      new Tester.Expectation("{serviceC=[], serviceD=[], serviceB=[serviceD, serviceC], serviceA=[serviceB, serviceC]}")),
                         Arguments.of("mtad-04-processed-after.yaml",
                                      new Tester.Expectation("{serviceA=[], serviceB=[], serviceC=[], serviceD=[]}")),
                         Arguments.of("mtad-05-processed-after.yaml",
                                      new Tester.Expectation("{serviceD=[], serviceC=[serviceD], serviceB=[serviceC, serviceD], serviceA=[serviceB]}")));
    }

    @ParameterizedTest
    @MethodSource
    void groupResourcesByWeightTest(String deploymentDescriptorLocation, Tester.Expectation expectation) {
        String deploymentDescriptorYaml = TestUtil.getResourceAsString(deploymentDescriptorLocation, getClass());
        DeploymentDescriptor deploymentDescriptor = new DescriptorParserFacade().parseDeploymentDescriptor(deploymentDescriptorYaml);
        List<Resource> resultResourceList = new ArrayList<>();
        for (List<Resource> list : getDescriptorHandler().getResourcesForProcessing(deploymentDescriptor)) {
            resultResourceList.addAll(list);
        }
        tester.test(() -> getProcessedAfterMapString(resultResourceList), expectation);
    }

    private String getProcessedAfterMapString(List<? extends Resource> resources) {
        Map<String, List<String>> processedAfterMap = new LinkedHashMap<>();
        for (Resource resource : resources) {
            processedAfterMap.put(resource.getName(), ListUtils.emptyIfNull(resource.getProcessedAfter()));
        }
        return processedAfterMap.toString();
    }
}