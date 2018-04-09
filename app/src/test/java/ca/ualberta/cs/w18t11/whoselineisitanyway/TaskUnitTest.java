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
    public final void testGetTitle()
    {
        final String title = "title";
        Assert.assertEquals(title,
                new Task("taskId", "requesterUsername", title, "description").getTitle());
    }

    @Test
    public final void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        Assert.assertEquals(longerTitle,
                new Task("taskId", "requesterUsername", longerTitle, "description").getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        new Task("taskId", "requesterUsername", tooLongTitle, "description");
    }

    @Test
    public final void testGetDescription()
    {
        final String description = "description";
        Assert.assertEquals(description,
                new Task("taskId", "requesterUsername", "title", description).getDescription());
    }

    @Test
    public final void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        Assert.assertEquals(longerDescription,
                new Task("taskId", "requesterUsername", "title", longerDescription)
                        .getDescription());
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
        new Task("taskId", "requesterUsername", "title", tooLongDescription);
    }

    @Test
    public final void testGetStatus()
    {
        Assert.assertEquals(TaskStatus.REQUESTED,
                new Task("taskId", "requesterUsername", "title", "description").getStatus());
    }

    @Test
    public final void testGetRequesterId()
    {
        final String requesterUsername = "requesterUsername";
        Assert.assertEquals(requesterUsername,
                new Task("taskId", requesterUsername, "title", "description")
                        .getRequesterUsername());
    }

    @Test
    public final void testGetProviderId()
    {
        final String taskId = "taskId";
        final String providerUsername = "providerUsername";
        Assert.assertEquals(providerUsername,
                new Task(taskId, "requesterUsername", providerUsername,
                        new Bid[]{new Bid(providerUsername, taskId, BigDecimal.ONE)}, "title",
                        "description",
                        false).getProviderUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNoBids()
    {
        new Task("taskId", "requesterUsername", new Bid[0], "title", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNoProviderBid()
    {
        final String taskId = "taskId";
        final String taskProviderId = "taskProviderId";
        final String bidProviderId = "bidProviderId";
        new Task(taskId, "requesterUsername", taskProviderId,
                new Bid[]{new Bid(bidProviderId, taskId, BigDecimal.ONE)}, "title", "description",
                false);
    }

    @Test(expected = IllegalStateException.class)
    public final void testSubmitBidOnAssignedTask()
    {
        final String taskId = "taskId";
        final String providerUsername = "providerUsername";
        new Task(taskId, "requesterUsername", providerUsername,
                new Bid[]{new Bid(providerUsername, taskId, BigDecimal.ONE)}, "title",
                "description",
                false).submitBid(new Bid(providerUsername, taskId, BigDecimal.TEN));
    }

    @Test(expected = IllegalStateException.class)
    public final void testAssignProviderOnAssignedTask()
    {
        final String taskId = "taskId";
        final String providerUsername = "providerUsername";
        final String otherProviderId = "otherProviderId";
        new Task(taskId, "requesterUsername", providerUsername, new Bid[]{
                new Bid(providerUsername, taskId, BigDecimal.ONE),
                new Bid(otherProviderId, taskId, BigDecimal.TEN)
        }, "title", "description", false).assignProvider("otherProviderId");
    }

    @Test(expected = IllegalStateException.class)
    public final void testMarkDoneOnBiddedTask()
    {
        final String taskId = "taskId";
        new Task(taskId, "requesterUsername",
                new Bid[]{new Bid("providerUsername", taskId, BigDecimal.ONE)},
                "title", "description").markDone();
    }

    @Test
    public final void testTaskLifecycle()
    {
        final String taskId = "taskId";
        Task task = new Task(taskId, "requesterUsername", "title", "description");
        Assert.assertEquals(TaskStatus.REQUESTED, task.getStatus());
        final String providerUsername = "providerUsername";
        final Bid bid = new Bid(providerUsername, taskId, BigDecimal.ONE);
        task = task.submitBid(bid);
        Assert.assertEquals(TaskStatus.BIDDED, task.getStatus());
        Assert.assertNotNull(task.getBids());
        Assert.assertEquals(1, task.getBids().length);
        Assert.assertEquals(bid, task.getBids()[0]);
        task = task.assignProvider(providerUsername);
        Assert.assertEquals(TaskStatus.ASSIGNED, task.getStatus());
        Assert.assertEquals(providerUsername, task.getProviderUsername());
        task = task.markDone();
        Assert.assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public final void testElasticId()
    {
        final Task task = new Task("requesterUsername", "title", "description");
        Assert.assertNull(task.getElasticId());
        final String taskId = "taskId";
        task.setElasticId(taskId);
        Assert.assertEquals(taskId, task.getElasticId());
    }
}
