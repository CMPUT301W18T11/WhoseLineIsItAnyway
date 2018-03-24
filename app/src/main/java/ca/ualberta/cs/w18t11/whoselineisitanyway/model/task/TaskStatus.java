package ca.ualberta.cs.w18t11.whoselineisitanyway.model.task;

import android.support.annotation.NonNull;

public enum TaskStatus
{
    REQUESTED("Requested"),
    BIDDED("Bidded"),
    ASSIGNED("Assigned"),
    DONE("Done");

    private final String description;

    TaskStatus(@NonNull final String description)
    {
        this.description = description;
    }

    @NonNull
    @Override
    public final String toString()
    {
        return this.description;
    }
}
