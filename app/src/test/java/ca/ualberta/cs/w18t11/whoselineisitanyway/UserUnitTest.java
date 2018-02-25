package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

public final class UserUnitTest
{
    @Test
    public final void testGetEmailAddress()
    {
        final EmailAddress emailAddress = new EmailAddress("user", "gmail.com");
        Assert.assertEquals(emailAddress,
                new User(emailAddress, new PhoneNumber(1, 800, 777, 6543)).getEmailAddress());
    }

    @Test
    public final void testGetPhoneNumber()
    {
        final PhoneNumber phoneNumber = new PhoneNumber(50, 999, 323, 715);
        Assert.assertEquals(phoneNumber,
                new User(new EmailAddress("user", "gmail.com"), phoneNumber).getPhoneNumber());
    }
}
