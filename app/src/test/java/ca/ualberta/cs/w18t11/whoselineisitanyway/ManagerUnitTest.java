package ca.ualberta.cs.w18t11.whoselineisitanyway;

import junit.framework.Assert;

import org.junit.Test;

public class ManagerUnitTest
{
    @Test(expected = IndexOutOfBoundsException.class)
    final public void testGetOutOfRange()
    {
        Manager<Integer> manager = new Manager<Integer>();
        manager.get(1);
    }

    @Test
    final public void testGetTask()
    {
        Manager<Task> taskManager = new Manager<Task>();
        Task task = new Task("name","description");
        Task wrongTask = new Task("name","description");
        taskManager.add(task);
        Assert.assertEquals(task, taskManager.get(0));
        Assert.assertNotSame(wrongTask, taskManager.get(0));
    }
}
