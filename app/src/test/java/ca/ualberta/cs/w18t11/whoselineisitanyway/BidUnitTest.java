package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Test;
import org.junit.Assert;
import java.math.BigDecimal;


public class BidUnitTest
{
    @Test(expected = IllegalArgumentException.class)
    final public void testNegativValue()
    {
        BigDecimal negativeValue = new BigDecimal(-1);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) < 0);
        new Bid("", "", negativeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testZeroValue()
    {
        BigDecimal negativeValue = new BigDecimal(0);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) == 0);
        new Bid("", "", negativeValue);
    }
}
