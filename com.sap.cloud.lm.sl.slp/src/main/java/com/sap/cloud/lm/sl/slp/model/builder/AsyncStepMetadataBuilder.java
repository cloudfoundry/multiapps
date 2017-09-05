package com.sap.cloud.lm.sl.slp.model.builder;

import java.util.List;

import com.sap.cloud.lm.sl.slp.model.AsyncStepMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;

public interface AsyncStepMetadataBuilder extends StepMetadataBuilder {

    @Override
    AsyncStepMetadataBuilder id(String id);

    @Override
    AsyncStepMetadataBuilder displayName(String displayName);

    @Override
    AsyncStepMetadataBuilder description(String description);

    @Override
    AsyncStepMetadataBuilder children(List<StepMetadata> children);

    @Override
    AsyncStepMetadataBuilder progressWeight(double progressWeight);

    @Override
    AsyncStepMetadataBuilder visible(boolean visible);

    @Override
    AsyncStepMetadataBuilder children(StepMetadata... children);

    AsyncStepMetadataBuilder pollTaskId(String pollTaskId);

    AsyncStepMetadataBuilder childrenVisible(boolean childrenVisible);

    @Override
    AsyncStepMetadata build();

}
