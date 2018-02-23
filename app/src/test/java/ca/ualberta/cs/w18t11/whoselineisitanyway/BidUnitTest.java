package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Test;
import org.junit.Assert;
import java.math.BigDecimal;


public class BidUnitTest
{
    @Test
    final public void testGetProviderId()
    {
        final String id = "provider";
        Assert.assertEquals(id, new Bid(id, "task", new BigDecimal(1)).getProviderId());
    }

    @Test
    final public void testGetTaskId()
    {
        final String id = "task";
        Assert.assertEquals(id, new Bid("provider", id, new BigDecimal(1)).getTaskId());
    }

    @Test
    final public void testGetValue()
    {
        final BigDecimal value = new BigDecimal(1);
        Assert.assertEquals(value, new Bid("provider", "task", value).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testEmptyProviderId()
    {
        String emptyId = "";
        Assert.assertTrue(emptyId.isEmpty());
        new Bid(emptyId, "task", new BigDecimal(1));
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testNegativValue()
    {
        BigDecimal negativeValue = new BigDecimal(-1);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) < 0);
        new Bid("provider", "task", negativeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    final public void testZeroValue()
    {
        BigDecimal negativeValue = new BigDecimal(0);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) == 0);
        new Bid("provider", "task", negativeValue);
    }
}
