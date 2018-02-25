package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

public final class EmailAddressUnitTest
{
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
}
