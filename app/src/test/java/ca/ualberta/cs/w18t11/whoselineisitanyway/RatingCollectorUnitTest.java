package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

public final class RatingCollectorUnitTest
{
    @Test
    public final void testAddRating()
    {
        final RatingCollector coll = new RatingCollector();
        final Rating rate = new Rating(1,2,3);
        coll.addRating(rate);
        Assert.assertEquals(coll.getRating(0), rate);
    }

    @Test
    public final void testGetAvgTtc()
    {
        final Random posInt = new Random();
        final RatingCollector coll = new RatingCollector();
        int ttcSum = 0;

        for (int index = 0; index < 50; index += 1)
        {
            final int p1 = posInt.nextInt(5);
            final int p2 = posInt.nextInt(5);
            ttcSum += p2;
            final int p3 = posInt.nextInt(5);

            coll.addRating(new Rating(p1, p2, p3));
        }

        final int avg = ttcSum / coll.getRatingCount();
        Assert.assertEquals(coll.getAvgTtc(), avg);
    }

    @Test
    public final void testGtAvgProf()
    {
        final Random posInt = new Random();
        final RatingCollector coll = new RatingCollector();

        int profSum = 0;

        for (int index = 0; index < 50; index += 1)
        {
            final int p1 = posInt.nextInt(5);
            final int p2 = posInt.nextInt(5);
            final int p3 = posInt.nextInt(5);
            profSum += p3;

            coll.addRating(new Rating(p1, p2, p3));
        }

        final int avg = profSum / coll.getRatingCount();
        Assert.assertEquals(coll.getAvgProf(), avg);
    }

    @Test
    public final void testGetAvgQuality()
    {
        final Random posInt = new Random();
        final RatingCollector coll = new RatingCollector();

        int qualitySum = 0;

        for (int index = 0; index < 50; index += 1)
        {
            final int p1 = posInt.nextInt(5);
            final int p2 = posInt.nextInt(5);
            qualitySum += p1;
            final int p3 = posInt.nextInt(5);

            coll.addRating(new Rating(p1, p2, p3));
        }

        final int avg = qualitySum / coll.getRatingCount();
        Assert.assertEquals(coll.getAvgQuality(), avg);
    }

}