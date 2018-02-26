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

}