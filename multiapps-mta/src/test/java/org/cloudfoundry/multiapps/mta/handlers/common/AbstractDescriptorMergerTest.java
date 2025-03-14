package org.cloudfoundry.multiapps.mta.handlers.common;

import java.util.List;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.MtaTestUtil;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorMerger;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;

public abstract class AbstractDescriptorMergerTest {
    protected final DescriptorMerger descriptorMerger = createDescriptorMerger();
    protected final DescriptorParser descriptorParser = createDescriptorParser();

    protected abstract DescriptorMerger createDescriptorMerger();

    protected abstract DescriptorParser createDescriptorParser();

    protected void executeTestMerge(Tester tester, String deploymentDescriptorLocation, String[] extensionDescriptorLocations,
                                    Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser);
        List<ExtensionDescriptor> extensionDescriptors = parseExtensionDescriptor(extensionDescriptorLocations, descriptorParser);
        tester.test(() -> descriptorMerger.merge(deploymentDescriptor, extensionDescriptors), expectation);
    }

    private List<ExtensionDescriptor> parseExtensionDescriptor(String[] extensionDescriptorLocations, DescriptorParser descriptorParser) {
        return MtaTestUtil.loadExtensionDescriptors(extensionDescriptorLocations, descriptorParser, getClass());
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation, DescriptorParser descriptorParser) {
        return MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser, getClass());
    }

}
