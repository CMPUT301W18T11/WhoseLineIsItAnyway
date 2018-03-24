package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;

public final class BidUnitTest
{
    private static final String bidId = "bidId";

    private static final String providerId = "providerId";

    private static final String taskId = "taskId";

    private static final BigDecimal value = BigDecimal.ONE;

    @Test
    public final void testGetProviderId()
    {
        final String otherProviderId = "otherProviderId";
        Assert.assertEquals(otherProviderId,
                new Bid(otherProviderId, BidUnitTest.taskId, BidUnitTest.value).getProviderId());
    }

    @Test
    public final void testGetTaskId()
    {
        final String otherTaskId = "otherTaskId";
        Assert.assertEquals(otherTaskId,
                new Bid(BidUnitTest.providerId, otherTaskId, BidUnitTest.value).getTaskId());
    }

    @Test
    public final void testGetValue()
    {
        final BigDecimal otherValue = BigDecimal.TEN;
        Assert.assertEquals(otherValue,
                new Bid(BidUnitTest.providerId, BidUnitTest.taskId, otherValue).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyProviderId()
    {
        final String emptyProviderId = "";
        Assert.assertTrue(emptyProviderId.isEmpty());
        new Bid(emptyProviderId, BidUnitTest.taskId, BidUnitTest.value);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyTaskId()
    {
        String emptyTaskId = "";
        Assert.assertTrue(emptyTaskId.isEmpty());
        new Bid(BidUnitTest.providerId, emptyTaskId, BidUnitTest.value);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNegativeValue()
    {
        final BigDecimal negativeValue = new BigDecimal(-1);
        Assert.assertTrue(negativeValue.compareTo(BigDecimal.ZERO) < 0);
        new Bid(BidUnitTest.providerId, BidUnitTest.taskId, negativeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testZeroValue()
    {
        final BigDecimal zeroValue = BigDecimal.ZERO;
        Assert.assertTrue(zeroValue.compareTo(BigDecimal.ZERO) == 0);
        new Bid(BidUnitTest.providerId, BidUnitTest.taskId, zeroValue);
    }

    @Test
    public final void testElasticId()
    {
        final Bid bid = new Bid(BidUnitTest.providerId, BidUnitTest.taskId, BidUnitTest.value);
        Assert.assertNull(bid.getElasticId());
        bid.setElasticId(BidUnitTest.bidId);
        Assert.assertEquals(BidUnitTest.bidId, bid.getElasticId());
    }

    @Test
    public final void testEquals()
    {
        final String otherBidId = "otherBidId";
        final String otherProviderId = "otherProviderId";
        final Bid bid = new Bid(BidUnitTest.bidId, BidUnitTest.providerId, BidUnitTest.taskId,
                BidUnitTest.value);
        final Bid sameBid = new Bid(otherBidId, BidUnitTest.providerId, BidUnitTest.taskId,
                BidUnitTest.value);
        final Bid differentBid = new Bid(BidUnitTest.bidId, otherProviderId, BidUnitTest.taskId,
                BidUnitTest.value);
        Assert.assertEquals(bid, bid);
        Assert.assertEquals(bid, sameBid);
        Assert.assertEquals(sameBid, bid);
        Assert.assertNotEquals(bid, differentBid);
        Assert.assertNotEquals(differentBid, bid);
    }
}
