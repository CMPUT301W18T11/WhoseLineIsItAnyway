package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

public final class UserUnitTest
{
    @Test
    public final void testGetEmailAddress()
    {
        final EmailAddress emailAddress = new EmailAddress("user", "gmail.com");
        Assert.assertEquals(emailAddress,
                new User(emailAddress, new PhoneNumber(1, 800, 777, 6543), "username")
                        .getEmailAddress());
    }

    @Test
    public final void testGetPhoneNumber()
    {
        final PhoneNumber phoneNumber = new PhoneNumber(50, 999, 323, 715);
        Assert.assertEquals(phoneNumber,
                new User(new EmailAddress("user", "gmail.com"), phoneNumber, "username")
                        .getPhoneNumber());
    }

    @Test
    public final void testGetTasks()
    {
        final User user = new User(new EmailAddress("user", "gmail.com"),
                new PhoneNumber(3, 333, 333, 3333), "username");
        final Task[] requestedTasks = user.getRequestedTasks();
        final Task[] assignedTasks = user.getAssignedTasks();
        Assert.assertNotNull(requestedTasks);
        Assert.assertNotNull(assignedTasks);
        Assert.assertNotEquals(requestedTasks, assignedTasks);
    }
}
