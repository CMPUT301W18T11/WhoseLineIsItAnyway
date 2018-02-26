package ca.ualberta.cs.w18t11.whoselineisitanyway;

import static org.junit.Assert.*;
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
     * The code for checking bounds is identical for all of them, so if one throws except, fix all.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testNewRatingNegativeParam()
    {
        final int failInt = -1 * new Random().nextInt(Integer.MIN_VALUE);
        assertTrue(failInt < 0);
        new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values > 5 (Expected OOB) throws exception when creating new Rating
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testNewRatingPositiveOOB()
    {
        final Random randomPos = new Random();
        int failInt = 22;

        assertTrue(failInt > 5);
        new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values [0-5] (Expected Inbounds) successfully creates new Rating
     */
    @Test
    public final void testNewRatingZeroFive()
    {
        int p1 = 0;
        int p2 = 3;
        int p3 = 5;
        assertTrue(0 <= p1 && p1 <= 5);
        assertTrue(0 <= p2 && p2 <= 5);
        assertTrue(0 <= p3 && p3 <= 5);
        new Rating(p1, p2, p3);
    }

    // TTC Stuff
    @Test
    public final void testSetTtc() {
        final Rating rating = new Rating(0, 0, 0);
        assertEquals(rating.getTtcRating(), 0);
        for (int ttc = 0; ttc < 6; ttc++) {
            rating.setTtcRating(ttc);
            assertTrue("Try setTTC " + ttc, rating.getTtcRating() == ttc);

        }
    }

    // QUALITY STUFF
    @Test
    public final void testSetQuality()
    {
        final Rating rating = new Rating(0, 0, 0);
        assertEquals(rating.getQualityRating(), 0);

        for (int quality = 0; quality < 6; quality++) {
            rating.setTtcRating(quality);
            assertTrue("Try setQuality " + quality, rating.getTtcRating() == quality);

        }
    }

    // PROF STUFF
    @Test
    public final void testSetProf()
    {
        final Rating rating = new Rating(0, 0, 0);
        assertEquals(rating.getProfRating(), 0);
        
        for (int prof = 0; prof < 6; prof++) {
            rating.setProfRating(prof);
            assertTrue("Try setProf " + prof, rating.getProfRating() == prof);

        }
    }

    @Test
    public final void testGetAggregateRating()
    {
        for (int p1 = 0; p1 < 6; p1 ++) {
            for (int p2 = 0; p2 < 6; p2 ++) {
                for (int p3 = 0; p3 < 6; p3 ++) {
                    Rating rate = new Rating(p1, p2, p3);
                    int aggTrueValue = (int) ((p1 + p2 + p3) / 3);
                    assertTrue("test GetAggregateRating Q: " + p1 + " T:" + p2 + " P:" + p3, rate.getAggRating() == aggTrueValue);
                }
            }
        }
    }

}