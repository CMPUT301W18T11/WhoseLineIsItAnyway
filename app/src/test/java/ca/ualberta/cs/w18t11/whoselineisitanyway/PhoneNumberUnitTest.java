package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

public final class PhoneNumberUnitTest
{
    @Test
    public final void testGetCountryCode()
    {
        final int countryCode = 1;
        Assert.assertEquals(countryCode,
                new PhoneNumber(countryCode, 555, 123, 6789).getCountryCode());
    }
}
