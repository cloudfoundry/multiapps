package com.sap.cloud.lm.sl.slp.model;

import com.sap.cloud.lm.sl.slp.model.builder.LoopStepMetadataBuilder;

abstract class AbstractLoopStepMetadataBuilder<T extends LoopStepMetadata, BT extends LoopStepMetadataBuilder>
    extends AbstractStepMetadataBuilder<T, BT>implements LoopStepMetadataBuilder {

    protected String countVariable;

    @Override
    public BT countVariable(String countVariable) {
        this.countVariable = countVariable;
        return getThis();
    }

    @Override
    public T build() {
        T meta = super.build();
        meta.countVariable = countVariable;
        return meta;
    }

}
