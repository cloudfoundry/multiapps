package org.cloudfoundry.multiapps.mta.resolvers.common;

import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.handlers.DescriptorParserFacade;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.ReferencesUnescaper;

public abstract class AbstractReferencesUnescaperTest {
    private ReferencesUnescaper referencesUnescaper = new ReferencesUnescaper();

    public void executeTestUnescaping(Tester tester, String descriptorResource, Tester.Expectation expectation) {
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorResource);
        tester.test(() -> {
            referencesUnescaper.unescapeReferences(descriptor);
            return descriptor;
        }, expectation);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String descriptorResource) {
        String yaml = TestUtil.getResourceAsString(descriptorResource, getClass());
        return new DescriptorParserFacade().parseDeploymentDescriptor(yaml);
    }

}
