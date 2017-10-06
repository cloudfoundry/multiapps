package com.sap.cloud.lm.sl.slp.activiti.tasklist.factory;

/**
 * Utility class for calculating the progress of a step from the progress of its substeps
 */
class ProgressCalculator {

    private double total = 0;
    private double progress = 0;

    /**
     * Registers the progress of a substep
     * 
     * @param progress progress of a substep
     * @param weight the relative weight of the substep (compared to other substeps)
     */
    public void accumulateProgress(double progress, double weight) {
        this.total += weight;
        this.progress += progress * weight;
    }

    /**
     * Returns the calculated progress percentage
     * 
     * @return the calculated progress
     */
    public double getProgress() {
        return progress / total;
    }

    public double getRawProgress() {
        return progress;
    }

    public double getTotal() {
        return total;
    }

}
