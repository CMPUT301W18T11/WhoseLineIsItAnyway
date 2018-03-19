package ca.ualberta.cs.w18t11.whoselineisitanyway;

import junit.framework.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class ManagerUnitTest
{
    @Test(expected = IndexOutOfBoundsException.class)
    public final void testDeleteTask()
    {
        Manager<Task> manager = new Manager<Task>();
        Task task = new Task("add", "add", "");
        manager.add(task);
        Assert.assertEquals(task, manager.get(0));
        manager.delete(0);
        manager.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testDeleteBid()
    {
        Manager<Bid> manager = new Manager<Bid>();
        Bid bid = new Bid("add", "add", new BigDecimal(1));
        manager.add(bid);
        Assert.assertEquals(bid, manager.get(0));
        manager.delete(0);
        manager.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testDeleteOutOfRange()
    {
        Manager<Integer> manager = new Manager<Integer>();
        manager.delete(0);
    }

    @Test
    public final void testGetTask() {
        Manager<Task> taskManager = new Manager<Task>();
        Task addedTask = new Task("name", "description", "");
        Task wrongTask = new Task("name", "description", "");
        taskManager.add(addedTask);
        Assert.assertEquals(addedTask, taskManager.get(0));
        Assert.assertNotSame(wrongTask, taskManager.get(0));
    }

    @Test
    public final void testGetBid() {
        Manager<Bid> bidManager = new Manager<Bid>();
        Bid addedBid = new Bid("provider", "task", new BigDecimal(1));
        Bid wrongBid = new Bid("provider", "task", new BigDecimal(1));
        bidManager.add(addedBid);
        Assert.assertEquals(addedBid, bidManager.get(0));
        Assert.assertNotSame(wrongBid, bidManager.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testGetOutOfRange() {
        Manager<Integer> manager = new Manager<Integer>();
        manager.get(1);
    }

    @Test
    public final void testSetTask()
    {
        Manager<Task> manager = new Manager<Task>();
        Task addedTask = new Task("add", "add", "");
        Task setTask = new Task("set", "set", "");
        manager.add(addedTask);
        Assert.assertEquals(addedTask, manager.get(0));
        manager.set(0, setTask);
        Assert.assertEquals(setTask, manager.get(0));
    }

    @Test
    public final void testSetBid()
    {
        Manager<Bid> manager = new Manager<Bid>();
        Bid addedBid = new Bid("add", "add", new BigDecimal(1));
        Bid setBid = new Bid("set", "set", new BigDecimal(1));
        manager.add(addedBid);
        Assert.assertEquals(addedBid, manager.get(0));
        manager.set(0, setBid);
        Assert.assertEquals(setBid, manager.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testSetOutOfRange() {
        Manager<Integer> manager = new Manager<Integer>();
        manager.set(1, new Integer(1));
    }
}
