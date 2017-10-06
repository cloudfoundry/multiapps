package com.sap.cloud.lm.sl.slp.model;

import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.slp.model.builder.ServiceMetadataBuilder;
import com.sap.lmsl.slp.SlpTaskType;

/**
 * Provides the metadata for a deployed process. Any process that needs to be exposed through the SL Protocol needs to be registered at
 * runtime by calling {@link com.sap.cloud.lm.sl.slp.ServiceRegistry#addService(ServiceMetadata)}.
 */
public class ServiceMetadata extends StepMetadata {

    protected String implementationId;
    protected Set<ParameterMetadata> parameters;
    protected boolean beta;
    protected String requiredPermission;
    protected List<String> versions;

    public static ServiceMetadataBuilder builder() {
        return new Builder();
    }

    private static class Builder extends AbstractServiceMetadataBuilder<ServiceMetadata, ServiceMetadataBuilder> {

        @Override
        protected ServiceMetadata createInstance() {
            return new ServiceMetadata();
        }

    }

    protected ServiceMetadata() {
    }

    protected ServiceMetadata(ServiceMetadata original) {
        super(original);
        this.implementationId = original.implementationId;
        this.parameters = original.parameters;
        this.requiredPermission = original.requiredPermission;
        this.beta = original.beta;
        this.versions = original.versions;
    }

    public Set<ParameterMetadata> getParameters() {
        return parameters;
    }

    public String getImplementationId() {
        return implementationId;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public List<String> getVersions() {
        return versions;
    }

    public boolean isBetaService() {
        return beta;
    }

    @Override
    public SlpTaskType getTaskType() {
        return SlpTaskType.SLP_TASK_TYPE_PROCESS;
    }

    @Override
    public ServiceMetadata getCopy() {
        return new ServiceMetadata(this);
    }

}
