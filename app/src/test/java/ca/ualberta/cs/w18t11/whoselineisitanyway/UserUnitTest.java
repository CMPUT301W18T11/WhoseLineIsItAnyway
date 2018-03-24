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

    public static final class UserUnitTest
    {
        private static final String userId = "userId";

        private static final String username = "username";

        private static final EmailAddress emailAddress = new EmailAddress("user", "gmail.com");

        private static final PhoneNumber phoneNumber = new PhoneNumber(1, 800, 777, 6543);

        @Test
        public final void testGetUsername()
        {
            final String otherUsername = "bob";
            Assert.assertEquals(otherUsername,
                    new User(otherUsername, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber)
                            .getUsername());
        }

        @Test
        public final void testGetEmailAddress()
        {
            final EmailAddress otherEmailAddress = new EmailAddress("bob", "hotmail.com");
            Assert.assertEquals(otherEmailAddress,
                    new User(ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.username, otherEmailAddress, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber)
                            .getEmailAddress());
        }

        @Test
        public final void testGetPhoneNumber()
        {
            final PhoneNumber otherPhoneNumber = new PhoneNumber(50, 999, 323, 715);
            Assert.assertEquals(otherPhoneNumber,
                    new User(ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.username, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress, otherPhoneNumber)
                            .getPhoneNumber());
        }

        @Test
        public final void testElasticId()
        {
            final User user = new User(
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.username, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress,
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber);
            Assert.assertNull(user.getElasticId());
            user.setElasticId(ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.userId);
            Assert.assertEquals(ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.userId, user.getElasticId());
        }

        @Test
        public final void testEquals()
        {
            final String otherUserId = "otherUserId";
            final String otherUsername = "otherUsername";
            final User user = new User(
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.userId, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.username,
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber);
            final User sameUser = new User(otherUserId, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.username,
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber);
            final User differentUser = new User(
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.userId, otherUsername,
                    ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.emailAddress, ca.ualberta.cs.w18t11.whoselineisitanyway.UserUnitTest.UserUnitTest.phoneNumber);
            Assert.assertEquals(user, user);
            Assert.assertEquals(user, sameUser);
            Assert.assertEquals(sameUser, user);
            Assert.assertNotEquals(user, differentUser);
            Assert.assertNotEquals(differentUser, user);
        }
    }
}
