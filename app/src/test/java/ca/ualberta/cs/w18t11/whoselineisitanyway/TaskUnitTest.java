package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

final public class TaskUnitTest
{
    @Test
    final public void testGetTitle()
    {
        final String title = "title";
        Assert.assertEquals(title, new Task(title, "").getTitle());
    }

    @Test
    final public void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        Assert.assertEquals(longerTitle, new Task(longerTitle, "").getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        new Task(tooLongTitle, "");
    }

    @Test
    final public void testGetDescription()
    {
        final String description = "description";
        Assert.assertEquals(description, new Task("", description).getDescription());
    }

    @Test
    final public void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        Assert.assertEquals(longerDescription,
                new Task("", longerDescription).getDescription());
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
        new Task("", tooLongDescription);
    }

    @Test
    final public void testGetStatus()
    {
        Assert.assertEquals(TaskStatus.REQUESTED, new Task("", "").getStatus());
    }
}
