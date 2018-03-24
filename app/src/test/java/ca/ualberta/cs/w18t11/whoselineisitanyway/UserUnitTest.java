package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

public final class UserUnitTest
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
                new User(otherUsername, UserUnitTest.emailAddress, UserUnitTest.phoneNumber)
                        .getUsername());
    }

    @Test
    public final void testGetEmailAddress()
    {
        final EmailAddress otherEmailAddress = new EmailAddress("bob", "hotmail.com");
        Assert.assertEquals(otherEmailAddress,
                new User(UserUnitTest.username, otherEmailAddress, UserUnitTest.phoneNumber)
                        .getEmailAddress());
    }

    @Test
    public final void testGetPhoneNumber()
    {
        final PhoneNumber otherPhoneNumber = new PhoneNumber(50, 999, 323, 715);
        Assert.assertEquals(otherPhoneNumber,
                new User(UserUnitTest.username, UserUnitTest.emailAddress, otherPhoneNumber)
                        .getPhoneNumber());
    }

    @Test
    public final void testElasticId()
    {
        final User user = new User(
                UserUnitTest.username, UserUnitTest.emailAddress,
                UserUnitTest.phoneNumber);
        Assert.assertNull(user.getElasticId());
        user.setElasticId(UserUnitTest.userId);
        Assert.assertEquals(UserUnitTest.userId, user.getElasticId());
    }

    @Test
    public final void testEquals()
    {
        final String otherUserId = "otherUserId";
        final String otherUsername = "otherUsername";
        final User user = new User(
                UserUnitTest.userId, UserUnitTest.username,
                UserUnitTest.emailAddress, UserUnitTest.phoneNumber);
        final User sameUser = new User(otherUserId, UserUnitTest.username,
                UserUnitTest.emailAddress, UserUnitTest.phoneNumber);
        final User differentUser = new User(
                UserUnitTest.userId, otherUsername,
                UserUnitTest.emailAddress, UserUnitTest.phoneNumber);
        Assert.assertEquals(user, user);
        Assert.assertEquals(user, sameUser);
        Assert.assertEquals(sameUser, user);
        Assert.assertNotEquals(user, differentUser);
        Assert.assertNotEquals(differentUser, user);
    }
}
