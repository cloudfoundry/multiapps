package org.cloudfoundry.multiapps.common;

/**
 * A runtime exception that signals Flowable to move the job to dead-letter state without logging anything to the operation log or progress
 * message database. Used when a step returns StepPhase.RETRY to stop the process cycle cleanly.
 */
public class StepPhaseRetryException extends RuntimeException {

    public StepPhaseRetryException(String message) {
        super(message);
    }

}
