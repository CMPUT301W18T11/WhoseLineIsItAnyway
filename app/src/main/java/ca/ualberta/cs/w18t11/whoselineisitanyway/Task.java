package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Task is a class for storing and managing the information assciated with a task created by
 * a user of the app.
 * Each task has a title, description, and status
 * The status of a Task may be one of the following
 * <ul>
 *     <li>REQUESTED</li>
 *     <li>BIDDED</li>
 *     <li>ASSIGNED</li>
 *     <li>DONE</li>
 * </ul>
 * @author Samuel Dolha
 * @version 1.0
 * @see TaskStatus
 */
final class Task implements Detailable, Serializable
{
    private final String title;

    private final String description;

    private final TaskStatus status;

    private final UUID id;

    private final String requesterId;

    private final String providerId;

    /**
     * Constructor for creating Task object.
     * Ensures title length and description length are within bounds.
     * @param title Title representing the Task
     * @param description Description of Task details
     * @param requesterId unique identifier of the requester
     * @param providerId unique identifier of the provider
     * @throws IllegalArgumentException Either title or description are outside specified bounds
     */
    Task(final String title, final String description, String requesterId, String providerId) throws IllegalArgumentException
    {
        if (title.length() > 30)
        {
            throw new IllegalArgumentException("Task title cannot exceed 30 characters");
        }

        if (description.length() > 300)
        {
            throw new IllegalArgumentException("Task description cannot exceed 300 characters");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.REQUESTED;
        this.requesterId = requesterId;
        this.providerId = providerId;
    }

    /**
     * Constructor for creating Task object.
     * Ensures title length and description length are within bounds.
     * Uses a null providerId to signify that there is no provider associated to the task
     * @param title Title representing the Task
     * @param description Description of Task details
     * @param requesterId unique identifier of the requester
     * @throws IllegalArgumentException Either title or description are outside specified bounds
     */
    Task(final String title, final String description, String requesterId) throws IllegalArgumentException
    {
        this(title, description, requesterId, null);
    }



    /**
     * Get the title of the Task
     * @return String representation of the Task's title
     */
    final String getTitle()
    {
        return this.title;
    }

    /**
     * Get the descri[tion of the Task
     * @return String representation of the Task's description
     */
    final String getDescription()
    {
        return this.description;
    }

    /**
     * Get the current status of the Task
     * @return TaskStatus representation of the Task's status
     */
    final TaskStatus getStatus()
    {
        return this.status;
    }

    /**
     * Get the id of the Task
     * @return String representation of the Task's id
     */
    final UUID getId()
    {
        return this.id;
    }

    /**
     * Get the current status of the Task
     * @return TaskStatus representation of the Task's status
     */
    final String getRequesterId()
    {
        return this.requesterId;
    }

    /**
     * Get the current status of the Task
     * @return TaskStatus representation of the Task's status
     */
    final String getProviderId()
    {
        return this.providerId;
    }

    @Override
    public <T extends DetailActivity> void showDetail(Class<T> detailActivityClass, Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("Title", getTitle()));
        detailList.add(new Detail("Description", getDescription()));
        detailList.add(new Detail("Status", getStatus().name()));

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailable.DATA_DETAIL_LIST, detailList);
        intent.putExtra(Detailable.DATA_DETAIL_TITLE, "Task");

        context.startActivity(intent);
    }

    /**
     * Provide a string to describe a bid
     * @return String representing the bid
     */
    @Override
    public String toString()
    {
        return this.getTitle();
    }
}
