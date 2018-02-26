package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

/**
 * RatingUnitTest is the Junit Testing class for <b>Rating</b>
 * @author Lucas Thalen and Samuel Dolha
 * @version 0.1
 * @see Rating
 */
public final class RatingUnitTest
{
    /**
     * Test to see if negative values (Expected OOB) throws exception when creating new Rating
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testNewRatingNegativeParam()
    {
        final int failInt = -1 * new Random().nextInt(Integer.MIN_VALUE);
        Assert.assertTrue(failInt < 0);
        new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values > 5 (Expected OOB) throws exception when creating new Rating
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testNewRatingPositiveOOB()
    {
        final Random randomPos = new Random();
        int failInt = randomPos.nextInt(Integer.MAX_VALUE);

        while (failInt < 5)
        {
            failInt = randomPos.nextInt(Integer.MAX_VALUE);
        }

        Assert.assertTrue(failInt > 5);
        new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values [0-5] (Expected Inbounds) successfully creates new Rating
     */
    @Test
    public final void testNewRatingZeroFive()
    {
        final Random randomPos = new Random();

        for (int index = 0; index < 100; index += 1)
        {
            final int p1 = randomPos.nextInt(5);
            final int p2 = randomPos.nextInt(5);
            final int p3 = randomPos.nextInt(5);
            Assert.assertTrue(0 <= p1 && p1 <= 5);
            Assert.assertTrue(0 <= p2 && p2 <= 5);
            Assert.assertTrue(0 <= p3 && p3 <= 5);
            new Rating(p1, p2, p3);
        }
    }

    // TTC Stuff
    @Test
    public final void testGetTtc()
    {
        final Random randomPos = new Random();

        for (int index = 0; index < 100; index += 1)
        {
            final int ttc = randomPos.nextInt(5);
            final Rating rating = new Rating(0, ttc,0);
            Assert.assertEquals(rating.getTtcRating(), ttc);
        }
    }

    @Test
    public final void testSetTtc()
    {
        final Random posInt = new Random();
        final Rating rating = new Rating(0, 0, 0);
        Assert.assertEquals(rating.getTtcRating(), 0);

        for (int index = 0; index < 100; index += 1)
        {
            final int ttc = posInt.nextInt(5);
            rating.setTtcRating(ttc);
            Assert.assertEquals(rating.getTtcRating(), ttc);
        }
    }

    // QUALITY STUFF
    @Test
    public final void testGetQuality()
    {
        final Random randomPos = new Random();

        for (int index = 0; index < 100; index += 1)
        {
            final int quality = randomPos.nextInt(5);
            final Rating rating = new Rating(quality, 0,0);
            Assert.assertEquals(rating.getQualityRating(), quality);
        }
    }

    @Test
    public final void testSetQuality()
    {
        final Random posInt = new Random();
        final Rating rating = new Rating(0, 0, 0);
        Assert.assertEquals(rating.getQualityRating(), 0);

        for (int index = 0; index < 100; index += 1)
        {
            final int quality = posInt.nextInt(5);
            rating.setQualityRating(quality);
            Assert.assertEquals(rating.getQualityRating(), quality);
        }
    }

    // PROF STUFF
    @Test
    public final void testGetProf()
    {
        final Random randomPos = new Random();
        for (int index = 0; index < 100; index += 1)
        {
            final int prof = randomPos.nextInt(5);
            final Rating rating = new Rating(0, 0,prof);
            Assert.assertEquals(rating.getProfRating(), prof);
        }
    }

    @Test
    public final void testSetProf()
    {
        final Random posInt = new Random();
        final Rating rating = new Rating(0, 0, 0);
        Assert.assertEquals(rating.getProfRating(), 0);

        for (int index = 0; index < 100; index += 1)
        {
            final int prof = posInt.nextInt(5);
            rating.setProfRating(prof);
            Assert.assertEquals(rating.getProfRating(), prof);
        }
    }

    @Test
    public final void testGetAggregateRating()
    {
        final Random posInt = new Random();

        for (int index = 0; index < 100; index += 1)
        {
            final int p1 = posInt.nextInt(5);
            final int p2 = posInt.nextInt(5);
            final int p3 = posInt.nextInt(5);
            final Rating rating = new Rating(p1, p2, p3);
            Assert.assertEquals(rating.getAggRating(), (int)((p1 + p2 + p3) / 3));
        }
    }

}