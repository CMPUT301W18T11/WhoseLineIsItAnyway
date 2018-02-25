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
}
