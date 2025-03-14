package org.cloudfoundry.multiapps.mta.handlers.common;

import java.util.List;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.MtaTestUtil;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDescriptorValidatorTest {
    private static final String PLATFORM_LOCATION = "/mta/sample/platform-01.json";
    private Platform platform;
    protected final DescriptorValidator validator = createDescriptorValidator();
    protected final DescriptorParser parser = createDescriptorParser();

    protected abstract DescriptorValidator createDescriptorValidator();

    protected abstract DescriptorParser createDescriptorParser();

    @BeforeEach
    public void setUp() {
        platform = MtaTestUtil.loadPlatform(PLATFORM_LOCATION, getClass());
    }

    public void executeTestValidateDeploymentDescriptor(Tester tester, String deploymentDescriptorLocation,
                                                        Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        tester.test(() -> validator.validateDeploymentDescriptor(deploymentDescriptor, platform), expectation);
    }

    public void executeTestValidateExtensionDescriptors(Tester tester, String deploymentDescriptorLocation,
                                                        String[] extensionDescriptorLocations, Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        List<ExtensionDescriptor> extensionDescriptors = parseExtensionDescriptors(extensionDescriptorLocations);
        tester.test(() -> validator.validateExtensionDescriptors(extensionDescriptors, deploymentDescriptor), expectation);
    }

    public void executeTestValidateMergedDescriptor(Tester tester, String deploymentDescriptorLocation, Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        tester.test(() -> validator.validateMergedDescriptor(deploymentDescriptor), expectation);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        return MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, parser, getClass());
    }

    private List<ExtensionDescriptor> parseExtensionDescriptors(String[] extensionDescriptorLocations) {
        return MtaTestUtil.loadExtensionDescriptors(extensionDescriptorLocations, parser, getClass());
    }
}
