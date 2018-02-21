package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class Task
{
    final private String title;

    Task(String title, String description)
    {
        this.title = title;
    }

    String getTitle()
    {
        return this.title;
    }
}
