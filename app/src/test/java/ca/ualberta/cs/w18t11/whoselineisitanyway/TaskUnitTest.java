package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

final public class TaskUnitTest
{
    @Test
    final public void testGetTitle()
    {
        final String title = "title";
        final Task task = new Task(title, "");
        Assert.assertEquals(title, task.getTitle());
    }

    @Test
    final public void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        final Task task = new Task(longerTitle, "");
        Assert.assertEquals(longerTitle, task.getTitle());
    }
}
