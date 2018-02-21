package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class Task
{
    final private String title;

    Task(final String title, final String description) throws IllegalArgumentException
    {
        if (title.length() > 30)
        {
            throw new IllegalArgumentException("Task title cannot exceed 30 characters");
        }

        this.title = title;
    }

    final String getTitle()
    {
        return this.title;
    }

    final String getDescription()
    {
        return "description";
    }
}
