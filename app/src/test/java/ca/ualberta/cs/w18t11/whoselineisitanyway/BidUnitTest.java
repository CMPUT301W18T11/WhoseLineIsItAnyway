package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;

public final class BidUnitTest
{
    @Test
    public final void testGetProviderId()
    {
        final String id = "provider";
        Assert.assertEquals(id, new Bid(id, "task", BigDecimal.ONE).getProviderId());
    }

    @Test
    public final void testGetTaskId()
    {
        final String id = "task";
        Assert.assertEquals(id, new Bid("provider", id, BigDecimal.ONE).getTaskId());
    }

    @Test
    public final void testGetValue()
    {
        final BigDecimal value = BigDecimal.ONE;
        Assert.assertEquals(value, new Bid("provider", "task", value).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyProviderId()
    {
        String emptyId = "";
        Assert.assertTrue(emptyId.isEmpty());
        new Bid(emptyId, "task", BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyTaskId()
    {
        String emptyId = "";
        Assert.assertTrue(emptyId.isEmpty());
        new Bid("provider", emptyId, BigDecimal.ONE);
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
        final BigDecimal zeroValue = BigDecimal.ZERO;
        Assert.assertTrue(zeroValue.compareTo(BigDecimal.ZERO) == 0);
        new Bid("provider", "task", zeroValue);
    }
}
