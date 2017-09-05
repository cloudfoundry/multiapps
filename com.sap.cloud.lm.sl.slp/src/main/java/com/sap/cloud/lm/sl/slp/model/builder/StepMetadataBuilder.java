package com.sap.cloud.lm.sl.slp.model.builder;

import java.util.List;

import com.sap.cloud.lm.sl.slp.model.StepMetadata;
import com.sap.lmsl.slp.SlpTaskState;

public interface StepMetadataBuilder {

    StepMetadataBuilder id(String id);

    StepMetadataBuilder displayName(String displayName);

    StepMetadataBuilder description(String description);

    StepMetadataBuilder targetState(SlpTaskState targetState);

    StepMetadataBuilder children(List<StepMetadata> children);

    StepMetadataBuilder progressWeight(double progressWeight);

    StepMetadataBuilder visible(boolean visible);

    StepMetadataBuilder children(StepMetadata... children);

    StepMetadata build();

}
