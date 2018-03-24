package ca.ualberta.cs.w18t11.whoselineisitanyway.model.task;

import android.support.annotation.NonNull;

/**
 * Represents the status of a task.
 *
 * @author Samuel Dolha
 * @version 2.0
 */
public enum TaskStatus
{
    /**
     * The initial status of a task, having no bids.
     */
    REQUESTED("Requested"),

    /**
     * The status of a task with at least one bid.
     */
    BIDDED("Bidded"),

    /**
     * The status of a task with a provider.
     */
    ASSIGNED("Assigned"),

    /**
     * The status of a completed task.
     */
    DONE("Done");

    /**
     * A string representation of the task's status.
     */
    private final String representation;

    /**
     * Creates a task's status.
     *
     * @param representation A string representation of the task's status.
     */
    TaskStatus(@NonNull final String representation)
    {
        this.representation = representation;
    }

    /**
     * @return A string representation of a task's status.
     * @see Object
     */
    @NonNull
    @Override
    public final String toString()
    {
        return this.representation;
    }
}
