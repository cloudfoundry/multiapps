package com.sap.cloud.lm.sl.slp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sap.cloud.lm.sl.slp.model.builder.AsyncStepMetadataBuilder;
import com.sap.cloud.lm.sl.slp.model.builder.StepMetadataBuilder;

public class AsyncStepMetadata extends StepMetadata {

    protected String pollTaskId;
    protected boolean childrenVisible;

    public static AsyncStepMetadataBuilder builder() {
        return new Builder();
    }

    private static class Builder extends AbstractAsyncStepMetadataBuilder<AsyncStepMetadata, AsyncStepMetadataBuilder> {

        @Override
        protected AsyncStepMetadata createInstance() {
            return new AsyncStepMetadata();
        }

    }

    protected AsyncStepMetadata() {
    }

    protected AsyncStepMetadata(AsyncStepMetadata original) {
        super(original);
        this.pollTaskId = original.pollTaskId;
        this.childrenVisible = original.childrenVisible;
    }

    @Override
    public List<StepMetadata> getChildren(VariableHandler variableHandler) {
        StepMetadataBuilder pollStepBuilder = StepMetadata.builder();
        pollStepBuilder.id(pollTaskId);
        pollStepBuilder.displayName(getDisplayName() + " (poll)");
        pollStepBuilder.description(getDescription() + " (poll)");
        pollStepBuilder.visible(childrenVisible);
        StepMetadata pollStep = pollStepBuilder.build();
        pollStep.setParent(this);
        return Arrays.asList(pollStep);
    }

    public String getPollTaskId() {
        return pollTaskId;
    }

    @Override
    public List<String> getStepIds() {
        List<String> result = new ArrayList<>();
        result.add(this.getId());
        result.add(this.pollTaskId);
        return result;
    }

    @Override
    public AsyncStepMetadata getCopy() {
        return new AsyncStepMetadata(this);
    }

}
