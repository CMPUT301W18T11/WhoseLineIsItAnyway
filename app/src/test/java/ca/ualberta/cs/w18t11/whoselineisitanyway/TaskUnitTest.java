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
}
