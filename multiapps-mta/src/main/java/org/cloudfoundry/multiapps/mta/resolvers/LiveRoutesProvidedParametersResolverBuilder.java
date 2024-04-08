package org.cloudfoundry.multiapps.mta.resolvers;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;

public class LiveRoutesProvidedParametersResolverBuilder {

    private final DeploymentDescriptor descriptor;
    private final String useLiveRoutesParameterKeyName;

    public LiveRoutesProvidedParametersResolverBuilder(DeploymentDescriptor descriptor, String useLiveRoutesParameterKeyName) {
        this.descriptor = descriptor;
        this.useLiveRoutesParameterKeyName = useLiveRoutesParameterKeyName;
    }

    public LiveRoutesProvidedParametersResolver build() {
        return new LiveRoutesProvidedParametersResolver(descriptor, useLiveRoutesParameterKeyName);
    }
}
