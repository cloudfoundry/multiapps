package com.sap.cloud.lm.sl.slp.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.slp.model.builder.StepMetadataBuilder;
import com.sap.lmsl.slp.SlpTaskState;
import com.sap.lmsl.slp.SlpTaskType;

/**
 * Describes a step of a process.
 */
public class StepMetadata {

    protected StepMetadata parent;

    protected String id;
    protected String displayName;
    protected String description;
    protected SlpTaskState targetState;
    protected List<StepMetadata> children;
    protected boolean visible;
    protected double progressWeight;

    public static StepMetadataBuilder builder() {
        return new Builder();
    }

    private static class Builder extends AbstractStepMetadataBuilder<StepMetadata, StepMetadataBuilder> {

        @Override
        protected StepMetadata createInstance() {
            return new StepMetadata();
        }

    }

    protected StepMetadata() {
    }

    protected StepMetadata(StepMetadata original) {
        this.visible = original.visible;
        this.id = original.id;
        this.displayName = original.displayName;
        this.description = original.description;
        this.targetState = original.targetState;
        this.children = original.children;
        this.progressWeight = original.progressWeight;
        this.parent = original.parent;
    }

    public StepMetadata getParent() {
        return parent;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public SlpTaskState getTargetState() {
        return targetState;
    }

    public List<StepMetadata> getChildren(VariableHandler variableHandler) {
        return children;
    }

    public double getProgressWeight() {
        return progressWeight;
    }

    public boolean isVisible() {
        return visible;
    }

    void setProgressWeight(double progressWeight) {
        this.progressWeight = progressWeight;
    }

    void setParent(StepMetadata parent) {
        this.parent = parent;
    }

    public SlpTaskType getTaskType() {
        return SlpTaskType.SLP_TASK_TYPE_STEP;
    }

    public List<String> getStepIds() {
        List<String> result = new ArrayList<String>();
        result.add(this.getId());
        for (StepMetadata child : children) {
            result.addAll(child.getStepIds());
        }
        return result;
    }

    public StepMetadata getCopy() {
        return new StepMetadata(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StepMetadata other = (StepMetadata) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StepMetadata [id=" + id + ", displayName=" + displayName + ", parent=" + parent + "]";
    }

}
