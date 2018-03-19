package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public final class TaskUnitTest
{
    @Test
    public final void testGetTitle()
    {
        final String title = "title";
        Assert.assertEquals(title, new Task(title, "", "").getTitle());
    }

    @Test
    public final void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        Assert.assertEquals(longerTitle, new Task(longerTitle, "", "").getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        new Task(tooLongTitle, "", "");
    }

    @Test
    public final void testGetDescription()
    {
        final String description = "description";
        Assert.assertEquals(description, new Task("", description, "").getDescription());
    }

    @Test
    public final void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        Assert.assertEquals(longerDescription,
                new Task("", longerDescription, "").getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testDescriptionTooLong()
    {
        final String tooLongDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Assert.assertTrue(tooLongDescription.length() > 300);
        new Task("", tooLongDescription, "");
    }

    @Test
    public final void testGetStatus()
    {
        Assert.assertEquals(TaskStatus.REQUESTED, new Task("", "", "").getStatus());
    }

    @Test
    public final void testGetId()
    {
        Assert.assertEquals(UUID.class, new Task("", "", "").getId().getClass());
    }

    @Test
    public final void testGetRequesterId()
    {
        Assert.assertEquals("Test", new Task("", "", "Test").getRequesterId());
    }

    @Test
    public final void testGetProviderId()
    {
        Assert.assertEquals("Test", new Task("", "", "", "Test").getProviderId());
    }

    @Test
    public final void testNullProviderId()
    {
        Assert.assertNull(new Task("", "", "").getProviderId());
    }
}
