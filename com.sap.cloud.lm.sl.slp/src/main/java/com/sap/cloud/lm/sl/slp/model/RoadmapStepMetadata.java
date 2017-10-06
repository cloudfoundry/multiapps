package com.sap.cloud.lm.sl.slp.model;

import com.sap.cloud.lm.sl.slp.model.builder.RoadmapStepMetadataBuilder;
import com.sap.lmsl.slp.SlpTaskType;

/**
 * Provides the metadata for a roadmap step.
 */
public class RoadmapStepMetadata extends StepMetadata {

    public static RoadmapStepMetadataBuilder builder() {
        return new Builder();
    }

    private static class Builder extends AbstractRoadmapStepMetadataBuilder<RoadmapStepMetadata, RoadmapStepMetadataBuilder> {

        @Override
        protected RoadmapStepMetadata createInstance() {
            return new RoadmapStepMetadata();
        }

    }

    protected RoadmapStepMetadata() {
    }

    protected RoadmapStepMetadata(RoadmapStepMetadata original) {
        super(original);
    }

    @Override
    public SlpTaskType getTaskType() {
        return SlpTaskType.SLP_TASK_TYPE_ROADMAP_USER;
    }

    @Override
    public RoadmapStepMetadata getCopy() {
        return new RoadmapStepMetadata(this);
    }

}
