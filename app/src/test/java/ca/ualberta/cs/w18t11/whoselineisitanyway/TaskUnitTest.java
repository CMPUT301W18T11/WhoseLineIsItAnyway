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
                new Task("taskId", "requesterId", title, "description").getTitle());
    }

    @Test
    public final void testGetLongerTitle()
    {
        final String longerTitle = "longer title";
        Assert.assertEquals(longerTitle,
                new Task("taskId", "requesterId", longerTitle, "description").getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTitleTooLong()
    {
        final String tooLongTitle = "terrible title that is too long";
        Assert.assertTrue(tooLongTitle.length() > 30);
        new Task("taskId", "requesterId", tooLongTitle, "description");
    }

    @Test
    public final void testGetDescription()
    {
        final String description = "description";
        Assert.assertEquals(description,
                new Task("taskId", "requesterId", "title", description).getDescription());
    }

    @Test
    public final void testGetLongerDescription()
    {
        final String longerDescription = "longer description";
        Assert.assertEquals(longerDescription,
                new Task("taskId", "requesterId", "title", longerDescription).getDescription());
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
        new Task("taskId", "requesterId", "title", tooLongDescription);
    }

    @Test
    public final void testGetStatus()
    {
        Assert.assertEquals(TaskStatus.REQUESTED,
                new Task("taskId", "requesterId", "title", "description").getStatus());
    }

    @Test
    public final void testGetRequesterId()
    {
        final String requesterId = "requesterId";
        Assert.assertEquals(requesterId,
                new Task("taskId", requesterId, "title", "description").getRequesterId());
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

    @Test(expected = IllegalArgumentException.class)
    public final void testNoBids()
    {
        new Task("taskId", "requesterId", new Bid[0], "title", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNoProviderBid()
    {
        final String taskId = "taskId";
        final String taskProviderId = "taskProviderId";
        final String bidProviderId = "bidProviderId";
        new Task(taskId, "requesterId", taskProviderId,
                new Bid[]{new Bid(bidProviderId, taskId, BigDecimal.ONE)}, "title", "description",
                false);
    }

    @Test(expected = IllegalStateException.class)
    public final void testSubmitBidOnAssignedTask()
    {
        final String taskId = "taskId";
        final String providerId = "providerId";
        new Task(taskId, "requesterId", providerId,
                new Bid[]{new Bid(providerId, taskId, BigDecimal.ONE)}, "title", "description",
                false).submitBid(new Bid(providerId, taskId, BigDecimal.TEN));
    }

    @Test(expected = IllegalStateException.class)
    public final void testAssignProviderOnAssignedTask()
    {
        final String taskId = "taskId";
        final String providerId = "providerId";
        final String otherProviderId = "otherProviderId";
        new Task(taskId, "requesterId", providerId, new Bid[]{
                new Bid(providerId, taskId, BigDecimal.ONE),
                new Bid(otherProviderId, taskId, BigDecimal.TEN)
        }, "title", "description", false).assignProvider("otherProviderId");
    }

    @Test(expected = IllegalStateException.class)
    public final void testMarkDoneOnBiddedTask()
    {
        final String taskId = "taskId";
        new Task(taskId, "requesterId", new Bid[]{new Bid("providerId", taskId, BigDecimal.ONE)},
                "title", "description").markDone();
    }

    @Test
    public final void testTaskLifecycle()
    {
        final String taskId = "taskId";
        Task task = new Task(taskId, "requesterId", "title", "description");
        Assert.assertEquals(TaskStatus.REQUESTED, task.getStatus());
        final String providerId = "providerId";
        final Bid bid = new Bid(providerId, taskId, BigDecimal.ONE);
        task = task.submitBid(bid);
        Assert.assertEquals(TaskStatus.BIDDED, task.getStatus());
        Assert.assertNotNull(task.getBids());
        Assert.assertEquals(1, task.getBids().length);
        Assert.assertEquals(bid, task.getBids()[0]);
        task = task.assignProvider(providerId);
        Assert.assertEquals(TaskStatus.ASSIGNED, task.getStatus());
        Assert.assertEquals(providerId, task.getProviderId());
        task = task.markDone();
        Assert.assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public final void testElasticId()
    {
        final Task task = new Task("requesterId", "title", "description");
        Assert.assertNull(task.getElasticId());
        final String taskId = "taskId";
        task.setElasticId(taskId);
        Assert.assertEquals(taskId, task.getElasticId());
    }

    @Test
    public final void testEquals()
    {
        final String taskId = "taskId";
        final String requesterId = "requesterId";
        final String otherRequesterId = "otherRequesterId";
        final String title = "title";
        final String description = "description";
        final Task task = new Task(taskId, requesterId, title, description);
        final Task sameTask = new Task(taskId, requesterId, title, description);
        final Task differentTask = new Task(taskId, otherRequesterId, title, description);
        Assert.assertEquals(task, task);
        Assert.assertEquals(task, sameTask);
        Assert.assertEquals(sameTask, task);
        Assert.assertNotEquals(task, differentTask);
        Assert.assertNotEquals(differentTask, task);
    }
}
