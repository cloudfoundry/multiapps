package com.sap.cloud.lm.sl.slp.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.slp.model.builder.StepMetadataBuilder;
import com.sap.lmsl.slp.SlpTaskState;

abstract class AbstractStepMetadataBuilder<T extends StepMetadata, BT extends StepMetadataBuilder> implements StepMetadataBuilder {

    protected String id;
    protected String displayName;
    protected String description;
    protected SlpTaskState targetState;
    protected List<StepMetadata> children;
    protected Double progressWeight;
    protected Boolean visible;

    @Override
    public BT id(String id) {
        this.id = id;
        return getThis();
    }

    @Override
    public BT displayName(String displayName) {
        this.displayName = displayName;
        return getThis();
    }

    @Override
    public BT description(String description) {
        this.description = description;
        return getThis();
    }

    @Override
    public BT targetState(SlpTaskState targetState) {
        this.targetState = targetState;
        return getThis();
    }

    @Override
    public BT children(List<StepMetadata> children) {
        this.children = children;
        return getThis();
    }

    @Override
    public BT progressWeight(double progressWeight) {
        this.progressWeight = progressWeight;
        return getThis();
    }

    @Override
    public BT visible(boolean visible) {
        this.visible = visible;
        return getThis();
    }

    @Override
    public BT children(StepMetadata... children) {
        return this.children(Arrays.asList(children));
    }

    @Override
    public T build() {
        T meta = createInstance();
        meta.progressWeight = CommonUtil.getOrDefault(progressWeight, 1.0);
        meta.id = id;
        meta.children = CommonUtil.getOrDefault(children, Collections.<StepMetadata> emptyList());
        meta.displayName = displayName;
        meta.description = description;
        meta.targetState = targetState;
        meta.visible = CommonUtil.getOrDefault(visible, true);
        for (StepMetadata child : meta.children) {
            child.setParent(meta);
        }
        return meta;
    }

    protected abstract T createInstance();

    @SuppressWarnings("unchecked")
    protected BT getThis() {
        return (BT) this;
    }

}
