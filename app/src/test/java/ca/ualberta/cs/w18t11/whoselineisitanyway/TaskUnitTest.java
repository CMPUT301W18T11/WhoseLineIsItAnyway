package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;

public final class TaskUnitTest
{
    @Test
    public final void testGetId()
    {
        final String id = "id";
        Assert.assertEquals(id, new Task(id, "requesterId", "title", "description").getId());
    }

    @Test
    public final void testGetTitle()
    {
        final String title = "title";
        Assert.assertEquals(title, new Task("id", "requesterId", title, "description").getTitle());
    }

    @Test
    public final void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        Assert.assertEquals(longerTitle,
                new Task("id", "requesterId", longerTitle, "description").getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        new Task("0", "requesterId", tooLongTitle, "description");
    }

    @Test
    public final void testGetDescription()
    {
        final String description = "description";
        Assert.assertEquals(description,
                new Task("0", "requesterId", "title", description).getDescription());
    }

    @Test
    public final void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        Assert.assertEquals(longerDescription,
                new Task("0", "requesterId", "title", longerDescription).getDescription());
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
        new Task("id", "requesterId", "title", tooLongDescription);
    }

    @Test
    public final void testGetStatus()
    {
        Assert.assertEquals(TaskStatus.REQUESTED,
                new Task("id", "requesterId", "title", "description").getStatus());
    }

    @Test
    public final void testGetRequesterId()
    {
        final String requesterId = "requesterId";
        Assert.assertEquals(requesterId,
                new Task("id", requesterId, "title", "description").getRequesterId());
    }

    @Test
    public final void testGetProviderId()
    {
        final String taskId = "taskId";
        final String providerId = "providerId";
        Assert.assertEquals(providerId, new Task(taskId, "requesterId", providerId,
                new Bid[]{new Bid(providerId, taskId, BigDecimal.ONE)}, "title", "description",
                false).getProviderId());
    }
}
