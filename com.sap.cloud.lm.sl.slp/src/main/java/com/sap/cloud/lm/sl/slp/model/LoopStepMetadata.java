package com.sap.cloud.lm.sl.slp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.slp.model.builder.LoopStepMetadataBuilder;

/**
 * A metadata class for steps that need to iterate through their set of substeps for a number of iterations that is unknown at design-time.
 * The step will generate multiple instances of it child steps at runtime, depending on a countVariable that is retrieved from a
 * VariableHandler.
 */
public class LoopStepMetadata extends StepMetadata {

    protected String countVariable;

    public static LoopStepMetadataBuilder builder() {
        return new Builder();
    }

    private static class Builder extends AbstractLoopStepMetadataBuilder<LoopStepMetadata, LoopStepMetadataBuilder> {

        @Override
        protected LoopStepMetadata createInstance() {
            return new LoopStepMetadata();
        }

    }

    protected LoopStepMetadata() {
    }

    protected LoopStepMetadata(LoopStepMetadata original) {
        super(original);
        this.countVariable = original.countVariable;
    }

    @Override
    public List<StepMetadata> getChildren(VariableHandler variableHandler) {
        List<Object> iterationsCountList = variableHandler.getVariablesIncludingFromSubProcesses(countVariable);
        if (CommonUtil.isNullOrEmpty(iterationsCountList)) {
            return Collections.emptyList();
        }
        Integer iterationsCount = mergeIterationsCount(iterationsCountList);
        List<StepMetadata> result = new ArrayList<>();
        for (int i = 0; i < iterationsCount; ++i) {
            for (StepMetadata child : this.children) {
                result.add(adjustChildProgressWeight(child, iterationsCount));
            }
        }
        return result;
    }

    private Integer mergeIterationsCount(List<Object> iterationsCountList) {
        Integer result = 0;
        for (Object iteration : iterationsCountList) {
            Integer iterationInteger = (Integer) iteration;
            result += iterationInteger;
        }
        return result;
    }

    private StepMetadata adjustChildProgressWeight(StepMetadata child, int iterationsCount) {
        StepMetadata childCopy = child.getCopy();
        childCopy.setProgressWeight(child.getProgressWeight() / iterationsCount);
        return childCopy;
    }

    @Override
    public LoopStepMetadata getCopy() {
        return new LoopStepMetadata(this);
    }

}
