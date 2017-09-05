package com.sap.cloud.lm.sl.slp.model;

import com.sap.cloud.lm.sl.slp.model.builder.RoadmapStepMetadataBuilder;

abstract class AbstractRoadmapStepMetadataBuilder<T extends RoadmapStepMetadata, BT extends RoadmapStepMetadataBuilder>
    extends AbstractStepMetadataBuilder<T, BT>implements RoadmapStepMetadataBuilder {

}
