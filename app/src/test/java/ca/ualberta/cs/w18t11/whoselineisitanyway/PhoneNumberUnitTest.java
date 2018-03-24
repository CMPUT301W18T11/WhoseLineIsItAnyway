package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;

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

    @Test
    public final void testEquals()
    {
        final int countryCode = 1;
        final int areaCode = 555;
        final int exchangeCode = 123;
        final int lineNumber = 6789;
        final PhoneNumber firstNumber
                = new PhoneNumber(countryCode, areaCode, exchangeCode, lineNumber);
        final PhoneNumber secondNumber
                = new PhoneNumber(countryCode, areaCode, exchangeCode, lineNumber);
        final PhoneNumber thirdNumber
                = new PhoneNumber(2, areaCode, exchangeCode, lineNumber);
        final PhoneNumber fourthNumber
                = new PhoneNumber(countryCode, 888, exchangeCode, lineNumber);
        final PhoneNumber fifthNumber
                = new PhoneNumber(countryCode, areaCode, 633, 4444);
        final PhoneNumber sixthNumber
                = new PhoneNumber(countryCode, areaCode, exchangeCode, 4444);

        Assert.assertEquals(firstNumber, secondNumber);
        Assert.assertNotEquals(secondNumber, thirdNumber);
        Assert.assertNotEquals(thirdNumber, fourthNumber);
        Assert.assertNotEquals(fourthNumber, fifthNumber);
        Assert.assertNotEquals(fifthNumber, sixthNumber);
    }
}
