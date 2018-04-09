package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;

public final class EmailAddressUnitTest
{
    @Test
    public final void testFromString()
    {
        final String localPart = "user";
        final String domain = "domain";
        final EmailAddress emailAddress = EmailAddress
                .fromString(String.format(Locale.getDefault(), "%s@%s", localPart, domain));
//        Assert.assertEquals(localPart, emailAddress.getLocalPart());
        Assert.assertEquals(domain, emailAddress.getDomain());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFromMalformedString()
    {
        EmailAddress.fromString("userdomain");
    }

    @Test
    public final void testGetLocalPart()
    {
        final String localPart = "franklin";
        Assert.assertEquals(localPart, new EmailAddress(localPart, "domain.com").getLocalPart());
    }

    @Test
    public final void testGetDomain()
    {
        final String domain = "domain.com";
        Assert.assertEquals(domain, new EmailAddress("franklin", domain).getDomain());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyLocalPart()
    {
        new EmailAddress("", "domain.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyDomain()
    {
        new EmailAddress("franklin", "");
    }

    @Test
    public final void testToString()
    {
        Assert.assertEquals("franklin@domain.com",
                new EmailAddress("franklin", "domain.com").toString());
    }

    @Test
    public final void testEquals()
    {
        final String localPart = "franklin";
        final String domain = "domain.com";
        final EmailAddress firstEmail = new EmailAddress(localPart, domain);
        final EmailAddress secondEmail = new EmailAddress(localPart, domain);
        final EmailAddress thirdEmail = new EmailAddress("bob", domain);
        final EmailAddress fourthEmail = new EmailAddress(localPart, "other.gov");

        Assert.assertEquals(firstEmail.getLocalPart(), secondEmail.getLocalPart());
        Assert.assertEquals(firstEmail.getDomain(), secondEmail.getDomain());
        Assert.assertEquals(firstEmail, secondEmail);
        Assert.assertNotEquals(firstEmail.getLocalPart(), thirdEmail.getLocalPart());
        Assert.assertEquals(firstEmail.getDomain(), thirdEmail.getDomain());
        Assert.assertNotEquals(firstEmail, thirdEmail);
        Assert.assertEquals(firstEmail.getLocalPart(), fourthEmail.getLocalPart());
        Assert.assertNotEquals(firstEmail.getDomain(), fourthEmail.getDomain());
        Assert.assertNotEquals(firstEmail, fourthEmail);
        Assert.assertNotEquals(thirdEmail.getLocalPart(), fourthEmail.getLocalPart());
        Assert.assertNotEquals(thirdEmail.getDomain(), fourthEmail.getDomain());
        Assert.assertNotEquals(thirdEmail, fourthEmail);
    }
}
