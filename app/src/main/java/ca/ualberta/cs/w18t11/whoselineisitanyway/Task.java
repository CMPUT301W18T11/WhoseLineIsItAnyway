package ca.ualberta.cs.w18t11.whoselineisitanyway;

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
final class Task
{
    private final String title;

    private final String description;

    private final TaskStatus status;
    
    Task(final String title, final String description) throws IllegalArgumentException
    {
        if (title.length() > 30)
        {
            throw new IllegalArgumentException("Task title cannot exceed 30 characters");
        }

        if (description.length() > 300)
        {
            throw new IllegalArgumentException("Task description cannot exceed 300 characters");
        }

        this.title = title;
        this.description = description;
        this.status = TaskStatus.REQUESTED;
    }

    final String getTitle()
    {
        return this.title;
    }

    final String getDescription()
    {
        return this.description;
    }

    final TaskStatus getStatus()
    {
        return this.status;
    }

    final void setStatus(TaskStatus newStatus) { this.status = newStatus; }
}
