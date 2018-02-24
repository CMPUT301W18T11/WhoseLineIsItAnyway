package ca.ualberta.cs.w18t11.whoselineisitanyway;

import junit.framework.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class ManagerUnitTest
{
    @Test(expected = IndexOutOfBoundsException.class)
    final public void testGetOutOfRange() {
        Manager<Integer> manager = new Manager<Integer>();
        manager.get(1);
    }

    @Test
    final public void testGetTask() {
        Manager<Task> taskManager = new Manager<Task>();
        Task addedTask = new Task("name", "description");
        Task wrongTask = new Task("name", "description");
        taskManager.add(addedTask);
        Assert.assertEquals(addedTask, taskManager.get(0));
        Assert.assertNotSame(wrongTask, taskManager.get(0));
    }

    @Test
    final public void testGetBid() {
        Manager<Bid> taskManager = new Manager<Bid>();
        Bid addedBid = new Bid("provider", "task", new BigDecimal(1));
        Bid wrongBid = new Bid("provider", "task", new BigDecimal(1));
        taskManager.add(addedBid);
        Assert.assertEquals(addedBid, taskManager.get(0));
        Assert.assertNotSame(wrongBid, taskManager.get(0));
    }
}
