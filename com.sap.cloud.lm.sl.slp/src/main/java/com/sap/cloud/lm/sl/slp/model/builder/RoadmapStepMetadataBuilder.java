package com.sap.cloud.lm.sl.slp.model.builder;

import java.util.List;

import com.sap.cloud.lm.sl.slp.model.RoadmapStepMetadata;
import com.sap.cloud.lm.sl.slp.model.StepMetadata;

public interface RoadmapStepMetadataBuilder extends StepMetadataBuilder {

    @Override
    RoadmapStepMetadataBuilder id(String id);

    @Override
    RoadmapStepMetadataBuilder displayName(String displayName);

    @Override
    RoadmapStepMetadataBuilder description(String description);

    @Override
    RoadmapStepMetadataBuilder children(List<StepMetadata> children);

    @Override
    RoadmapStepMetadataBuilder progressWeight(double progressWeight);

    @Override
    RoadmapStepMetadataBuilder visible(boolean visible);

    @Override
    RoadmapStepMetadataBuilder children(StepMetadata... children);

    @Override
    RoadmapStepMetadata build();

}
