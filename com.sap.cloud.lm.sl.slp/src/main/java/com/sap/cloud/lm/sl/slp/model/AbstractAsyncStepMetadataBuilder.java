package com.sap.cloud.lm.sl.slp.model;

import java.util.Collections;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.slp.model.builder.AsyncStepMetadataBuilder;

abstract class AbstractAsyncStepMetadataBuilder<T extends AsyncStepMetadata, BT extends AsyncStepMetadataBuilder>
    extends AbstractStepMetadataBuilder<T, BT>implements AsyncStepMetadataBuilder {

    protected String pollTaskId;
    protected Boolean childrenVisible;

    @Override
    public BT pollTaskId(String pollTaskId) {
        this.pollTaskId = pollTaskId;
        return getThis();
    }

    @Override
    public BT childrenVisible(boolean childrenVisible) {
        this.childrenVisible = childrenVisible;
        return getThis();
    }

    @Override
    public T build() {
        T meta = super.build();
        meta.children = Collections.emptyList();
        meta.pollTaskId = pollTaskId;
        meta.childrenVisible = CommonUtil.getOrDefault(childrenVisible, false);
        return meta;
    }

}
