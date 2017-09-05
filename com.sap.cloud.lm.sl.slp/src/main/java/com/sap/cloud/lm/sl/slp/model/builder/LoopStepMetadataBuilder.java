package com.sap.cloud.lm.sl.slp.model.builder;

import java.util.List;

import com.sap.cloud.lm.sl.slp.model.LoopStepMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;

public interface LoopStepMetadataBuilder extends StepMetadataBuilder {

    LoopStepMetadataBuilder countVariable(String countVariable);

    @Override
    LoopStepMetadataBuilder id(String id);

    @Override
    LoopStepMetadataBuilder displayName(String displayName);

    @Override
    LoopStepMetadataBuilder description(String description);

    @Override
    LoopStepMetadataBuilder children(List<StepMetadata> children);

    @Override
    LoopStepMetadataBuilder progressWeight(double progressWeight);

    @Override
    LoopStepMetadataBuilder visible(boolean visible);

    @Override
    LoopStepMetadataBuilder children(StepMetadata... children);

    @Override
    LoopStepMetadata build();

}
