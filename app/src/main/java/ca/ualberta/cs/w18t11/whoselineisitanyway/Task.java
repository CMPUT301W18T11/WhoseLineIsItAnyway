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
    private String title;

    private String description;

    private TaskStatus status;

    /**
     * Constructor for creating Task object.
     * Ensures title length and description length are within bounds.
     * @param title Title representing the Task
     * @param description Description of Task details
     * @throws IllegalArgumentException Either title or description are outside specified bounds
     */
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

    /**
     * Get the title of the Task
     * @return String representation of the Task's title
     */
    final String getTitle()
    {
        return this.title;
    }

    /**
     * Get the description of the Task
     * @return String representation of the Task's description
     */
    final String getDescription()
    {
        return this.description;
    }

    /**
     * Get the status of the Task
     * @return TaskStatus object representing one of the 4 statuses
     */
    final TaskStatus getStatus() {
        return this.status;
    }
    /**
     * Sets the current status of the Task
     * @param newStatus the new status of the Task
     */
    final void setStatus(TaskStatus newStatus) { this.status = newStatus; }
}
