package com.sap.cloud.lm.sl.mta.resolvers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorReferenceResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorReferenceResolver {

    protected final ResolverBuilder resourcePropertiesResolverBuilder;

    public DescriptorReferenceResolver(DeploymentDescriptor descriptor, ResolverBuilder modulesPropertiesResolverBuilder,
        ResolverBuilder resourcePropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        super(descriptor, modulesPropertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
        this.resourcePropertiesResolverBuilder = resourcePropertiesResolverBuilder;
    }

    private DeploymentDescriptor getDescriptor() {
        return cast(this.descriptor);
    }

    @Override
    public DeploymentDescriptor resolve() throws ContentException {
        getDescriptor().setResources3(getResolvedResources());
        return cast(super.resolve());
    }

    private List<Resource> getResolvedResources() {
        List<Resource> resolvedResources = new ArrayList<>();
        for (Resource resource : getDescriptor().getResources3()) {
            resolvedResources.add(createResourceResolver(resource).resolve());
        }
        return resolvedResources;
    }

    protected ResourceReferenceResolver createResourceResolver(Resource resource) {
        return new ResourceReferenceResolver(getDescriptor(), resource, "", resourcePropertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder);
    }

}
