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

    @Test(expected = IllegalArgumentException.class)
    final public void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        final Task task = new Task(tooLongTitle, "");
    }

    @Test
    final public void testGetDescription()
    {
        final String description = "description";
        final Task task = new Task("", description);
        Assert.assertEquals(description, task.getDescription());
    }

    @Test
    final public void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        final Task task = new Task("", longerDescription);
        Assert.assertEquals(longerDescription, task.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testDescriptionTooLong()
    {
        final StringBuilder builder = new StringBuilder(301);
        final String segment = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Assert.assertEquals(segment.length(), 43);

        for (int index = 0; index < 301 / 43; index += 1)
        {
            builder.append(segment);
        }

        final String tooLongDescription = builder.toString();
        Assert.assertTrue(tooLongDescription.length() > 300);
        final Task task = new Task("", tooLongDescription);
    }
}
