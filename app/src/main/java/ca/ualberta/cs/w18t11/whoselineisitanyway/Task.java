package ca.ualberta.cs.w18t11.whoselineisitanyway;

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
}
