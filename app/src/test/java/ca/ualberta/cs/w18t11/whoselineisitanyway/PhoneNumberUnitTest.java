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

    @Test
    public final void testGetAreaCode()
    {
        final int areaCode = 555;
        Assert.assertEquals(areaCode, new PhoneNumber(1, areaCode, 123, 6789).getAreaCode());
    }

    @Test
    public final void testGetExchangeCode()
    {
        final int exchangeCode = 123;
        Assert.assertEquals(exchangeCode,
                new PhoneNumber(1, 555, exchangeCode, 6789).getExchangeCode());
    }

    @Test
    public final void testGetLineNumber()
    {
        final int lineNumber = 6789;
        Assert.assertEquals(lineNumber,
                new PhoneNumber(1, 555, 123, lineNumber).getLineNumber());
    }

    @Test
    public final void testToString()
    {
        Assert.assertEquals("+1 (555) 123-6789", new PhoneNumber(1, 555, 123, 6789).toString());
    }
}
