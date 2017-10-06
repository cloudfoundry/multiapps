package com.sap.cloud.lm.sl.slp.model.builder;

import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.slp.model.ParameterMetadata;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;

public interface ServiceMetadataBuilder extends StepMetadataBuilder {

    @Override
    ServiceMetadataBuilder id(String id);

    ServiceMetadataBuilder implementationId(String implementationId);

    @Override
    ServiceMetadataBuilder displayName(String displayName);

    @Override
    ServiceMetadataBuilder description(String description);

    @Override
    ServiceMetadataBuilder children(List<StepMetadata> children);

    @Override
    ServiceMetadataBuilder progressWeight(double progressWeight);

    @Override
    ServiceMetadataBuilder visible(boolean visible);

    @Override
    ServiceMetadataBuilder children(StepMetadata... children);

    ServiceMetadataBuilder requiredPermission(String requiredPermission);

    ServiceMetadataBuilder parameters(Set<ParameterMetadata> parameters);

    ServiceMetadataBuilder beta(boolean beta);

    ServiceMetadataBuilder versions(List<String> versions);

    ServiceMetadataBuilder versions(String... versions);

    @Override
    ServiceMetadata build();

}
