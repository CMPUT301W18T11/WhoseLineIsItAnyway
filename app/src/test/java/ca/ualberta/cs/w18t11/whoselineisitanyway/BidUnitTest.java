package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Test;
import org.junit.Assert;
import java.math.BigDecimal;

public final class BidUnitTest
{
    @Test
    public final void testGetProviderId()
    {
        final String id = "provider";
        Assert.assertEquals(id, new Bid(id, "task", new BigDecimal(1)).getProviderId());
    }

    @Test
    public final void testGetTaskId()
    {
        final String id = "task";
        Assert.assertEquals(id, new Bid("provider", id, new BigDecimal(1)).getTaskId());
    }

    @Test
    public final void testGetValue()
    {
        final BigDecimal value = new BigDecimal(1);
        Assert.assertEquals(value, new Bid("provider", "task", value).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyProviderId()
    {
        String emptyId = "";
        Assert.assertTrue(emptyId.isEmpty());
        new Bid(emptyId, "task", new BigDecimal(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyTaskId()
    {
        String emptyId = "";
        Assert.assertTrue(emptyId.isEmpty());
        new Bid("provider", emptyId, new BigDecimal(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNegativeValue()
    {
        final BigDecimal negativeValue = new BigDecimal(-1);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) < 0);
        new Bid("provider", "task", negativeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testZeroValue()
    {
        final BigDecimal zeroValue = new BigDecimal(0);
        Assert.assertTrue(zeroValue.compareTo(BigDecimal.ZERO) == 0);
        new Bid("provider", "task", zeroValue);
    }
}
